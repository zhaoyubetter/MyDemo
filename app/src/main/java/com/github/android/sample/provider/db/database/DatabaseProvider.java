package com.github.android.sample.provider.db.database;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;

import com.github.android.sample.provider.db.annotations.FieldFilter;
import com.github.android.sample.provider.db.annotations.PrimaryKey;
import com.github.android.sample.provider.db.annotations.Table;
import com.github.android.sample.provider.db.annotations.TableField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by momo on 2015/1/1.
 * Updatey by better on 2019/09/27
 * 统计信息内容提供者
 */
public abstract class DatabaseProvider extends ContentProvider {

    static final String RESET_DB = "resetDB";

    /**
     * 同步锁
     */
    private static final Object LOCK = new Object();
    private static final String HOST_CLASS = "/class:";
    // 用于在 ContentProvider 中注册URI, 根据 URI 匹配 ContentProvider 中对应的数据表
    private static final UriMatcher matcher;
    // 下标对应数据库表名
    private static final SparseArray<String> matchIds;
    // 下标（即表）对应数据库表的列
    private static final SparseArray<LinkedHashMap<String, String>> selectionMaps;
    private SQLiteOpenHelper myDatabase;// 数据库操作对象

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matchIds = new SparseArray<>();
        selectionMaps = new SparseArray<>();
    }

    public DatabaseProvider() {
    }

    /**
     * 获得 onCreate 时，初始化的 database helper 对象
     *
     * @param context
     * @param dbName  dbName
     * @return
     */
    public abstract SQLiteOpenHelper getSQLiteHelper(Context context, String dbName);

    /**
     * 获取 contentProvider#onCreate时的dbName
     *
     * @return
     */
    public abstract String getInitDbName();

    /**
     * change database helper, for example : change db
     */
    private void resetDB(Context context, String dbName) {
        synchronized (LOCK) {
            matchIds.clear();   // 切换表后，清空静态变量
            selectionMaps.clear();
            this.myDatabase = getSQLiteHelper(context, dbName);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        //初始化 DatabaseHelper对象
        DatabaseHelper.init(context);
        //初始化数据库对象
        myDatabase = getSQLiteHelper(context, getInitDbName());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final Uri newUri = ensureUri(uri);
        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        final int matchId = matcher.match(newUri);
        final String tableName = matchIds.get(matchId);
        final HashMap<String, String> map = selectionMaps.get(matchId);
        Cursor cursor = null;
        if (!TextUtils.isEmpty(tableName) && null != map) {
            builder.setTables(tableName);
            builder.setProjectionMap(map);
            // 判断uid
            SQLiteDatabase readableDatabase = getReadableDatabase();
            cursor = builder.query(readableDatabase, projection, selection, selectionArgs, null, null, sortOrder);
            if (null != cursor) {
                cursor.setNotificationUri(getContext().getContentResolver(), newUri);
            }
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final Uri newUri = ensureUri(uri);
        final int matchId = matcher.match(newUri);
        final String tableName = matchIds.get(matchId);
        Uri notifyUri = null;
        if (!TextUtils.isEmpty(tableName)) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            // 使用 insert or replace into
            long rowId = writableDatabase.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
//            long rowId = writableDatabase.insert(tableName, null, values);
            if (rowId > 0) {
                notifyUri = ContentUris.withAppendedId(newUri, rowId);
                getContext().getContentResolver().notifyChange(notifyUri, null);
            }
        }
        return notifyUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final Uri newUri = ensureUri(uri);
        final int matchId = matcher.match(newUri);
        final String tableName = matchIds.get(matchId);
        int rows = -1;
        if (!TextUtils.isEmpty(tableName)) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            rows = writableDatabase.delete(tableName, selection, selectionArgs);
            if (-1 != rows) {
                Uri notifyUri = ContentUris.withAppendedId(newUri, rows);
                getContext().getContentResolver().notifyChange(notifyUri, null);
            }
        }
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final Uri newUri = ensureUri(uri);
        final int matchId = matcher.match(newUri);
        final String tableName = matchIds.get(matchId);
        int rows = -1;
        if (!TextUtils.isEmpty(tableName)) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            rows = writableDatabase.update(tableName, values, selection, selectionArgs);
            if (-1 != rows) {
                Uri notifyUri = ContentUris.withAppendedId(newUri, rows);
                getContext().getContentResolver().notifyChange(notifyUri, null);
            }
        }
        return rows;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final Uri newUri = ensureUri(uri);
        final int matchId = matcher.match(newUri);
        final String tableName = matchIds.get(matchId);
        long lastId = -1;
        if (!TextUtils.isEmpty(tableName)) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            writableDatabase.beginTransaction();
            for (int i = 0; i < values.length; i++) {
                long rowId = writableDatabase.insert(tableName, null, values[i]);
                if (i == values.length - 1) {
                    lastId = rowId;
                }
                if (0 > rowId) {
                    //异常插入
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            if (lastId > 0) {
                Uri notifyUri = ContentUris.withAppendedId(newUri, lastId);
                getContext().getContentResolver().notifyChange(notifyUri, null);
            }
        }
        return (int) lastId;
    }

    private Uri ensureUri(Uri uri) {
        String path = uri.getPath();
        String authority = uri.getAuthority();
        Uri newUri;
        if (path.startsWith(HOST_CLASS)) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(path.substring(HOST_CLASS.length()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String tableName = DatabaseHelper.getTable(clazz);
            if (0 > matchIds.indexOfValue(tableName)) {
                synchronized (LOCK) {
                    //检测表是否存在,不存在创建
                    if (!tableExist(tableName)) {
                        createTable(clazz);
                    }
                    //添加表信息
                    addTable(clazz);
                }
            }
            newUri = Uri.parse("content://" + authority + "/" + tableName);
        } else {
            newUri = uri;
        }
        return newUri;
    }

    /**
     * 检测表是否存在
     *
     * @param tableName
     * @return
     */
    private final boolean tableExist(String tableName) {
        boolean result = false;
        if (!TextUtils.isEmpty(tableName)) {
            String sql = "select * from sqlite_master where name=" + "'" + tableName + "'";
            SQLiteDatabase readableDatabase = getReadableDatabase();
            Cursor cursor = null;
            try {
                cursor = readableDatabase.rawQuery(sql, null);
                if (cursor.moveToNext()) {
                    result = cursor.getCount() != 0;
                }
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }
        return result;
    }

    /**
     * 创建表
     *
     * @param clazz
     */
    private final void createTable(Class<?> clazz) {
        final Field[] fields = clazz.getDeclaredFields();
        // 属性上配置的了 @PrimaryKey
        final List<Pair<String, Boolean>> propPrimaryKeys = new ArrayList<>();
        final HashMap<String, String> fieldItems = new HashMap<>();
        // 联合主键
        String[] unionPrimaryKeys = null;
        boolean hasPrimaryKey = false;

        for (int i = 0; i < fields.length; i++) {
            Class<?> type = fields[i].getType();
            String fieldType;
            if (int.class == type || short.class == type || Integer.class == type || Short.class == type) {
                fieldType = " INTEGER";
            } else if (float.class == type || double.class == type || Float.class == type || Double.class == type) {
                fieldType = " FLOAT";
            } else if (boolean.class == type || Boolean.class == type) {
                fieldType = " BOOLEAN";
            } else if (long.class == type || Long.class == type) {
                fieldType = " LONG";
            } else {
                fieldType = " TEXT";
            }

            //过滤字段
            FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
            if (Modifier.STATIC != (fields[i].getModifiers() & Modifier.STATIC) && (null == fieldFilter || !fieldFilter.value())) {
                String fieldName = fields[i].getName();
                TableField tableField = fields[i].getAnnotation(TableField.class);
                PrimaryKey primaryKey = fields[i].getAnnotation(PrimaryKey.class);  // 是否配置了注解
                if (null != tableField) {
                    fieldName = TextUtils.isEmpty(tableField.value()) ? fields[i].getName() : tableField.value();
                }
                if (null != primaryKey) {
                    propPrimaryKeys.add(new Pair(fieldName, primaryKey.autoGenerate()));
                } else {
                    fieldItems.put(fieldName, fieldType);
                }
            }
        }
        String tableName = DatabaseHelper.getTable(clazz);
        String sql = "CREATE TABLE " + tableName + "(";
        // 字段主键只能有一个
        if (propPrimaryKeys.size() > 1) {
            throw new RuntimeException("table " + tableName + " has more than one primary key.");
        }
        if (propPrimaryKeys.size() == 1) {
            hasPrimaryKey = true;
            final Pair<String, Boolean> pair = propPrimaryKeys.get(0);
            sql += (pair.first + " INTEGER PRIMARY KEY " + (pair.second ? "AUTOINCREMENT" : "") + ",");
        }

        // 获取联合主键设置
        Table table = clazz.getAnnotation(Table.class);
        if (null != table && table.primaryKeys().length > 0) {
            unionPrimaryKeys = table.primaryKeys();
            if (hasPrimaryKey) {
                throw new RuntimeException("table " + tableName + " has more than one primary key.");
            }
        }

        // 组织列信息
        int index = 0;
        for (Map.Entry<String, String> entry : fieldItems.entrySet()) {
            sql += (entry.getKey() + " " + entry.getValue() + " " + (index++ != fieldItems.size() - 1 ? "," : " "));
        }
        // 可能有多个主键时,设置联合主键
        if (unionPrimaryKeys != null && unionPrimaryKeys.length > 0) {
            sql += (", PRIMARY KEY(");
            for (int i = 0; i < unionPrimaryKeys.length; i++) {
                String key = unionPrimaryKeys[i];
                sql += (key + (i != unionPrimaryKeys.length - 1 ? "," : "))"));
            }
        } else {
            sql += ")";
        }
        //创建建此表
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Log.e("better", sql);
        writableDatabase.execSQL(sql);
    }

    private void addTable(Class<?> clazz) {
        final Context context = getContext();
        final String tableName = DatabaseHelper.getTable(clazz);
        final String authority = DatabaseHelper.getAuthority(context);
        final int index = matchIds.size();
        //添加匹配uri
        matcher.addURI(authority, tableName, index + 1);
        //添加匹配表名
        matchIds.append(index + 1, tableName);
        //添加selectionMap
        String[] selection = DatabaseHelper.getSelection(clazz);
        LinkedHashMap<String, String> selectionMap = new LinkedHashMap<>();
        for (int s = 0; s < selection.length; s++) {
            selectionMap.put(selection[s], selection[s]);
        }
        selectionMaps.append(index + 1, selectionMap);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**
     * 获取一个读取的数据库对象
     */
    SQLiteDatabase getReadableDatabase() {
        if (null == myDatabase) {
            Context context = getContext();
            myDatabase = getSQLiteHelper(context,getInitDbName());
        }
        return myDatabase.getReadableDatabase();
    }

    /**
     * 获取一个读取的数据库对象
     */
    SQLiteDatabase getWritableDatabase() {
        if (null == myDatabase) {
            Context context = getContext();
            myDatabase = getSQLiteHelper(context, getInitDbName());
        }
        return myDatabase.getWritableDatabase();
    }

    /**
     * call useful
     *
     * @param method
     * @param arg
     * @param extras
     * @return
     */
    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Bundle bundle = new Bundle();
        bundle.putString("method", method);

        // 重新创建数据库，用途，app 使用过程中，需要切换数据库
        if (RESET_DB.equals(method) && !TextUtils.isEmpty(arg)) {
            resetDB(getContext(), arg);    // arg 为数据库名字
            bundle.putBoolean("result", true);
        }
        return bundle;
    }

    // 原生查询操作，注意这里暂时无法跨进程操作；
    public void execSQL(String sql) {
        if (myDatabase != null) {
            SQLiteDatabase writableDatabase = myDatabase.getWritableDatabase();
            writableDatabase.execSQL(sql);
        }
    }

    public void execSQL(String sql, String[] bindArgs) {
        if (myDatabase != null) {
            SQLiteDatabase writableDatabase = myDatabase.getWritableDatabase();
            writableDatabase.execSQL(sql, bindArgs);
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        if (myDatabase != null) {
            SQLiteDatabase readableDatabase = myDatabase.getReadableDatabase();
            return readableDatabase.rawQuery(sql, selectionArgs);
        }
        return null;
    }
}
