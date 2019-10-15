package com.github.android.sample.provider.db.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

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
    private SQLiteOpenHelper sqLiteOpenHelper;// 数据库操作对象
    private SQLiteDatabase writeDatabase;
    // 确保只使用一个数据库链接
    private AtomicInteger openCounter = new AtomicInteger();


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
            if(writeDatabase != null) {
                writeDatabase.close();
                writeDatabase = null;
            }
            openCounter.set(0); // 复位
            this.sqLiteOpenHelper = getSQLiteHelper(context, dbName);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        //初始化 DatabaseHelper对象
        DatabaseHelper.init(context);
        //初始化数据库对象
        sqLiteOpenHelper = getSQLiteHelper(context, getInitDbName());
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
            SQLiteDatabase db = getWritableDatabase();
            try {
                // 使用 insert or replace into
                long rowId = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (rowId > 0) {
                    notifyUri = ContentUris.withAppendedId(newUri, rowId);
                    getContext().getContentResolver().notifyChange(notifyUri, null);
                }
            } finally {
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
            try {
                rows = writableDatabase.delete(tableName, selection, selectionArgs);
                if (-1 != rows) {
                    Uri notifyUri = ContentUris.withAppendedId(newUri, rows);
                    getContext().getContentResolver().notifyChange(notifyUri, null);
                }
            } finally {
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
            try {
                rows = writableDatabase.update(tableName, values, selection, selectionArgs);
                if (-1 != rows) {
                    Uri notifyUri = ContentUris.withAppendedId(newUri, rows);
                    getContext().getContentResolver().notifyChange(notifyUri, null);
                }
            } finally {
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
            try {
                writableDatabase.beginTransaction();
                for (int i = 0; i < values.length; i++) {
                    long rowId = writableDatabase.insertWithOnConflict(tableName, null, values[i], SQLiteDatabase.CONFLICT_REPLACE);
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
            } finally {
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
        if (null == sqLiteOpenHelper) {
            Context context = getContext();
            sqLiteOpenHelper = getSQLiteHelper(context, getInitDbName());
        }
        return sqLiteOpenHelper.getReadableDatabase();
    }

    /**
     * 获取一个读取的数据库对象，我们确保链接使用同一个就行了
     */
    SQLiteDatabase getWritableDatabase() {
        if (null == sqLiteOpenHelper) {
            Context context = getContext();
            sqLiteOpenHelper = getSQLiteHelper(context, getInitDbName());
        }
        if (openCounter.incrementAndGet() == 1 || (writeDatabase == null || !writeDatabase.isOpen())) {
            writeDatabase = sqLiteOpenHelper.getWritableDatabase();
        }
        return writeDatabase;
    }

    /**
     * 不能关闭close db，会导致整个 sqliteHelper 都会广播
     * @param db
     */
    @Deprecated
    private synchronized void closeDB(SQLiteDatabase db) {
        if (openCounter.decrementAndGet() == 0) {
//            db.close();
        }
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
        if (sqLiteOpenHelper != null) {
            if (writeDatabase == null || !writeDatabase.isOpen()) {
                writeDatabase = getWritableDatabase();
            }
            writeDatabase.execSQL(sql);
        }
    }

    public void execSQL(String sql, String[] bindArgs) {
        if (sqLiteOpenHelper != null) {
            if (writeDatabase != null && !writeDatabase.isOpen()) {
                writeDatabase = getWritableDatabase();
            }
            writeDatabase.execSQL(sql);
        }
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        if (sqLiteOpenHelper != null) {
            SQLiteDatabase readableDatabase = getReadableDatabase();
            return readableDatabase.rawQuery(sql, selectionArgs);
        }
        return null;
    }
}
