package com.github.android.sample.provider.db.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;


import com.github.android.sample.provider.db.annotations.FieldFilter;
import com.github.android.sample.provider.db.annotations.PrimaryKey;
import com.github.android.sample.provider.db.annotations.Table;
import com.github.android.sample.provider.db.annotations.TableField;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author :Created by cz, better
 * @date 2019-06-21 16:40
 * 数据库操作对象
 */
public final class DatabaseHelper {
    private static final String TAG = "DatabaseHelper";
    /**
     * 保存字节码对象与uri路径
     */
    private static final Map<Class<?>, Uri> uriMap = new HashMap<>();
    /**
     * 单例内部对象
     */
    private static DatabaseHelper helper = new DatabaseHelper();
    /**
     * 操作上下文对象
     */
    private static Context appContext;
    /**
     * 数据库版本变化监听
     */
    private OnDatabaseUpgradeListener upgradeListener;

    /**
     * 初部初始化操作
     *
     * @param context
     */
    static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    private DatabaseHelper() {
    }

    /**
     * 获得操作对象
     * context 会来自不同进程
     *
     * @return
     */
    public static DatabaseHelper getInstance(Context context) {
        appContext = context.getApplicationContext();
        return helper;
    }

    /**
     * 插入数据
     *
     * @param item
     */
    public final Uri insert(Object item) {
        Uri rtnUri = null;
        if (null != item) {
            Uri uri = getObjectUri(item.getClass());
            if (null != appContext && null != uri) {
                ContentResolver resolver = appContext.getContentResolver();
                rtnUri = resolver.insert(uri, DatabaseHelper.getContentValue(item));
            }
        }
        return rtnUri;
    }

    /**
     * 批量插入对象
     *
     * @param items
     * @return
     */
    public final int bulkInsert(final List items) {
        int code = -1;
        if (null != items && !items.isEmpty()) {
            final Object obj = items.get(0);
            final Uri uri = getObjectUri(obj.getClass());
            final ContentValues[] values = new ContentValues[items.size()];
            for (int i = 0; i < items.size(); i++) {
                values[i] = DatabaseHelper.getContentValue(items.get(i));
            }
            if (null != appContext && null != uri) {
                ContentResolver resolver = appContext.getContentResolver();
                code = resolver.bulkInsert(uri, values);
            }
        }
        return code;
    }

    /**
     * 根据指定条件更新对象
     *
     * @param item
     * @param where
     * @param whereArgs
     * @return
     */
    public final int update(Object item, String where, String... whereArgs) {
        int code = -1;
        if (null != item) {
            Uri uri = getObjectUri(item.getClass());
            if (null != appContext && null != uri) {
                ContentResolver resolver = appContext.getContentResolver();
                code = resolver.update(uri, DatabaseHelper.getContentValue(item), where, whereArgs);
            }
        }
        return code;
    }

    /**
     * 根据对象属性删除此记录
     *
     * @param item
     * @return
     */
    public final int delete(Object item) {
        int code = -1;
        if (null != item) {
            Pair<String, String[]> where = getWhere(item);
            code = delete(item.getClass(), where.first, where.second);
        }
        return code;
    }

    /**
     * 根据条件删除指定对象
     *
     * @param where
     * @param whereArgs
     * @return
     */
    public final int delete(Class clazz, String where, String[] whereArgs) {
        int code = -1;
        if (null != clazz) {
            final Uri uri = getObjectUri(clazz);
            if (null != appContext && null != uri) {
                ContentResolver resolver = appContext.getContentResolver();
                code = resolver.delete(uri, where, whereArgs);
            }
        }
        return code;
    }

    /**
     * 根据指定对象,获得查询条件
     *
     * @param item
     * @return
     */
    final Pair<String, String[]> getWhere(Object item) {
        Pair<String, String[]> wherePair = new Pair<>(null, null);
        if (null != item) {
            Field[] fields = item.getClass().getDeclaredFields();
            HashMap<String, String> fieldItems = new HashMap<>();
            for (int i = 0; i < fields.length; i++) {
                FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
                if (Modifier.STATIC != (fields[i].getModifiers() & Modifier.STATIC) && (null == fieldFilter || !fieldFilter.value())) {
                    fields[i].setAccessible(true);
                    try {
                        Object value = fields[i].get(item);
                        if (null != value) {
                            fieldItems.put(fields[i].getName(), value.toString());
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            int index = 0;
            if (!fieldItems.isEmpty()) {
                String where = new String();
                String[] whereArgs = new String[fieldItems.size()];
                for (Map.Entry<String, String> entry : fieldItems.entrySet()) {
                    where += (entry.getKey() + "=? " + (index != fieldItems.size() - 1 ? "and " : ""));
                    whereArgs[index++] = entry.getValue();
                }
                wherePair = new Pair<>(where, whereArgs);
            }
        }
        return wherePair;
    }

    /**
     * 根据指定对象字节码文件,与查询条件查询指定对象
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public final <E> E query(Class<E> clazz, String where, String[] whereArgs, String order) {
        E item = null;
        final Uri uri = getObjectUri(clazz);
        if (null != appContext && null != uri) {
            ContentResolver resolver = appContext.getContentResolver();
            Cursor cursor = null;
            try {
                String[] selection = DatabaseHelper.getSelection(clazz);
                cursor = resolver.query(uri, selection, where, whereArgs, order);
                if (null != cursor && cursor.moveToFirst()) {
                    item = DatabaseHelper.getItemByCursor(clazz, cursor);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }
        return item;
    }

    /**
     * 查询条目个数
     *
     * @param clazz
     * @param <E>
     * @return
     */
    public final <E> int queryCount(Class<E> clazz) {
        int count = -1;
        Uri uri = getObjectUri(clazz);
        if (null != appContext && null != uri) {
            ContentResolver resolver = appContext.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (null != cursor) {
                    count = cursor.getCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }
        return count;
    }

    /**
     * 查询数据集
     *
     * @param clazz 当前操作对象字节码
     * @param <E>
     * @return
     */
    public final <E> List<E> queryList(Class<E> clazz) {
        return queryList(clazz, null, null, null);
    }

    /**
     * 查询数据集
     *
     * @param clazz 当前操作对象字节码
     * @param <E>
     * @return
     */
    public final <E> List<E> queryList(Class<E> clazz, String where, String[] whereArgs, String order) {
        List<E> items = new ArrayList<>();
        Uri uri = getObjectUri(clazz);
        if (null != appContext && null != uri) {
            ContentResolver resolver = appContext.getContentResolver();
            Cursor cursor = null;
            try {
                String[] selection = DatabaseHelper.getSelection(clazz);
                cursor = resolver.query(uri, selection, where, whereArgs, order);
                if (null != cursor) {
                    while (cursor.moveToNext()) {
                        items.add(DatabaseHelper.getItemByCursor(clazz, cursor));
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (null != cursor) {
                    cursor.close();
                }
            }
        }
        return items;
    }

    /**
     * 清空指定表
     *
     * @param clazz
     */
    public void truncate(Class<?> clazz) {
        Uri uri = getObjectUri(clazz);
        ContentResolver contentResolver = appContext.getContentResolver();
        contentResolver.delete(uri, null, null);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (null != upgradeListener) {
            upgradeListener.onUpgrade(db, oldVersion, newVersion);
        }
    }

    /**
     * 设置数据库升级监听
     *
     * @param listener
     */
    public void setOnDbUpgradeListener(OnDatabaseUpgradeListener listener) {
        this.upgradeListener = listener;
    }

    /**
     * 数据库版本变化监听
     */
    public interface OnDatabaseUpgradeListener {
        void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

    /**
     * 根据class获得访问uri地址
     *
     * @param clazz
     * @return
     */
    public static Uri getObjectUri(Class<?> clazz) {
        Uri tableUri = uriMap.get(clazz);
        if (null == tableUri) {
            tableUri = Uri.parse("content://" + appContext.getPackageName() + "/class:" + clazz.getName());
            uriMap.put(clazz, tableUri);
        }
        return tableUri;
    }

    /**
     * 根据原始使用的uri地址
     *
     * @param clazz
     * @return
     */
    public static Uri getUri(Class<?> clazz) {
        final String tableName = getTable(clazz);
        return Uri.parse("content://" + appContext.getPackageName() + "/" + tableName);
    }

    /**
     * 获得对象字段列
     *
     * @param clazz
     * @return
     */
    public static String[] getSelection(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        ArrayList<String> selectionLists = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            String fieldName;
            TableField tableField = fields[i].getAnnotation(TableField.class);
            if (null != tableField && !TextUtils.isEmpty(tableField.value())) {
                fieldName = tableField.value();
            } else {
                fieldName = fields[i].getName();
            }
            FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
            if (Modifier.STATIC != (fields[i].getModifiers() & Modifier.STATIC) && (null == fieldFilter || !fieldFilter.value())) {
                selectionLists.add(fieldName);
            }
        }
        return selectionLists.toArray(new String[selectionLists.size()]);
    }

    public static String getTable(Class<?> clazz) {
        String tableName;
        Table table = clazz.getAnnotation(Table.class);
        if (null != table && !TextUtils.isEmpty(table.value())) {
            tableName = table.value();
        } else {
            tableName = clazz.getSimpleName();
        }
        return tableName;
    }

    /**
     * 根据对象获得指定数据库ContentValues对象
     *
     * @param item
     * @return
     */
    public static ContentValues getContentValue(Object item) {
        final Class<?> clazz = item.getClass();
        final ContentValues values = new ContentValues();
        final Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name;
            TableField tableField = field.getAnnotation(TableField.class);
            if (null != tableField && !TextUtils.isEmpty(tableField.value())) {
                name = tableField.value();
            } else {
                name = field.getName();
            }
            Class<?> type = field.getType();
            FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
            if (Modifier.STATIC != (fields[i].getModifiers() & Modifier.STATIC) && (null == fieldFilter || !fieldFilter.value())) {
                try {
                    if (int.class == type || Integer.class == type) {
                        values.put(name, field.getInt(item));
                    } else if (short.class == type || Short.class == type) {
                        values.put(name, field.getShort(item));
                    } else if (float.class == type || Float.class == type) {
                        values.put(name, field.getFloat(item));
                    } else if (double.class == type || Double.class == type) {
                        values.put(name, field.getDouble(item));
                    } else if (boolean.class == type || Boolean.class == type) {
                        values.put(name, field.getBoolean(item));
                    } else if (long.class == type || Long.class == type) {
                        values.put(name, field.getLong(item));
                    } else if (null != field.get(item)) {
                        values.put(name, field.get(item).toString());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return values;
    }

    /**
     * 获得内容提供的path
     *
     * @return
     */
    static String getAuthority(Context context) {
        return context.getPackageName();
    }

    /**
     * 根据Cursor获得数据集
     *
     * @param clazz
     * @param cursor
     * @param <E>
     * @return
     */
    public static <E> List<E> getListByCursor(Class<E> clazz, Cursor cursor) {
        final List<E> items = new ArrayList<>();
        try {
            while (null != cursor && cursor.moveToNext()) {
                items.add(DatabaseHelper.getItemByCursor(clazz, cursor));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * 根据 class 映射cursor信息
     *
     * @param clazz
     * @param cursor
     * @param <E>
     */
    public static <E> E getItemByCursor(Class<E> clazz, Cursor cursor) throws InstantiationException, IllegalAccessException {
        final E item = clazz.newInstance();
        final Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name;
            TableField tableField = field.getAnnotation(TableField.class);
            if (null != tableField && !TextUtils.isEmpty(tableField.value())) {
                name = tableField.value();
            } else {
                name = field.getName();
            }
            Class<?> type = field.getType();
            FieldFilter fieldFilter = fields[i].getAnnotation(FieldFilter.class);
            if (Modifier.STATIC != (fields[i].getModifiers() & Modifier.STATIC) && (null == fieldFilter || !fieldFilter.value())) {
                int columnIndex = cursor.getColumnIndex(name);
                if (0 <= columnIndex) {
                    if (int.class == type || Integer.class == type) {
                        field.set(item, cursor.getInt(columnIndex));
                    } else if (short.class == type || Short.class == type) {
                        field.set(item, cursor.getShort(columnIndex));
                    } else if (float.class == type || Float.class == type) {
                        field.set(item, cursor.getFloat(columnIndex));
                    } else if (double.class == type || Double.class == type) {
                        field.set(item, cursor.getDouble(columnIndex));
                    } else if (boolean.class == type || Boolean.class == type) {
                        field.set(item, 1 == cursor.getInt(columnIndex));
                    } else if (long.class == type || Long.class == type) {
                        field.set(item, cursor.getLong(columnIndex));
                    } else {
                        field.set(item, cursor.getString(columnIndex));
                    }
                }
            }
        }
        return item;
    }

    // ==== content provider call method 的使用

    /**
     * 重置数据库
     *
     * @param dbName dbName
     */
    public final void resetDatabase(String dbName) {
        try {
            final Uri uri = Uri.parse("content://" + appContext.getPackageName());
            appContext.getContentResolver().call(uri, DatabaseProvider.RESET_DB, dbName, null);
        } catch (Exception e) {
            // 极少崩溃，外界调用，应避免一开始就调用 provider 的方法，避免provider未初始化完成
        }
    }

    // SqliteHelper#onCreate() 生成数据表

    /**
     * 创建表
     * s @param clazz
     */
    static final void createTable(Class<?> clazz, final SQLiteDatabase db) {
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
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append("`" + tableName + "`").append("(");
        // 字段主键只能有一个
        if (propPrimaryKeys.size() > 1) {
            throw new RuntimeException("table " + tableName + " has more than one primary key.");
        }
        if (propPrimaryKeys.size() == 1) {
            hasPrimaryKey = true;
            final Pair<String, Boolean> pair = propPrimaryKeys.get(0);
            sql.append("`" + pair.first + "`").append(" INTEGER PRIMARY KEY").append(pair.second ? "AUTOINCREMENT" : "").append(",");
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
            sql.append("`" + entry.getKey() + "`").append(entry.getValue()).append(" ").append((index++ != fieldItems.size() - 1 ? "," : " "));
        }
        // 可能有多个主键时,设置联合主键
        if (unionPrimaryKeys != null && unionPrimaryKeys.length > 0) {
            sql.append(", PRIMARY KEY(");
            for (int i = 0; i < unionPrimaryKeys.length; i++) {
                String key = unionPrimaryKeys[i];
                sql.append("`" + key  + "`").append(i != unionPrimaryKeys.length - 1 ? "," : "));");
            }
        } else {
            sql.append(");");
        }
        //创建建此表
        Log.e("better", sql.toString());
        db.execSQL(sql.toString());
    }

}

