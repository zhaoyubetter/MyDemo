package com.github.android.sample.provider.db.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.github.android.sample.provider.db.AuthEntity;
import com.github.android.sample.provider.db.Cat;
import com.github.android.sample.provider.db.User;
import com.github.android.sample.provider.db.User2;


/**
 * 需要确保单例模式
 *
 * @author :Created by cz, better
 * @date 2019-06-21 16:29
 * 数据库实现对象
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    /**
     * 当前数据库版本
     */
    private static final int CURRENT_VERSION = 4;
    private Context context;
    private volatile static MyDatabaseHelper instance;

    private MyDatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, CURRENT_VERSION);
        this.context = context.getApplicationContext();
    }

    /**
     * 根据数据库名，确保单例
     *
     * @param context
     * @param dbName
     * @return
     */
    public static MyDatabaseHelper getInstance(Context context, String dbName) {
        // 数据库名称不一样，表示切换数据库了
        if (instance != null && !instance.getDatabaseName().equals(dbName)) {
            synchronized (MyDatabaseHelper.class) {
                Log.e("better", String.format("prev: %s, current: %s", instance.getDatabaseName(), dbName));
                instance = null;
            }
        }
        if (instance == null) {
            synchronized (MyDatabaseHelper.class) {
                if (instance == null) {
                    instance = new MyDatabaseHelper(context, dbName);
                }
            }
        }

        return instance;
    }

    /**
     * 参考：
     * https://blog.csdn.net/feibendexiaoma/article/details/79526187
     * =============
     * onCreate 方法执行时机：
     * 数据库第一次创建的时候调用
     * getWritableDatabase()的时候才会创建数据库
     * <p>
     * 1、SQLiteOpenHelper会自动检测数据库文件是否存在。如果存在，会打开这个数据库，
     * 在这种情况下就不会调用onCreate()方法。如果数据库文件不存在，SQLiteOpenHelper首先会创建一个数据库文件，
     * 然后打开这个数据库，最后调用onCreate()方法。因此，onCreate()方法一般用来在新创建的数据库中建立表、视图等数据库组建。
     * 也就是说onCreate()方法在数据库文件第一次创建时调用。
     * <p>
     * 2、如果数据库文件不存在，只有onCreate()被调用（该方法在创建数据库时被调用一次）。
     * 如果数据库文件存在，会根据版本号是否有递增，进而调用 onUpdate()方法升级数据库，并更新版本号。
     * 3. 用户重新安装 app 时，只会走 onCreate() ，这里创新最新表即可；
     * <p>
     * ：
     * onUpgrade() 方法执行时机：
     * 用户从1.0版本升级到3.0版本 ，SQLiteOpenHelper 的继承类里会走 onUpgrade()方法,不走 onCreate() 方法。
     * 即 v1.0 —-> v3.0 走 onUpgrade() 这个时候需要设计好 onUpgrade() 方法
     * <p>
     * ===============================================================
     * <p>
     * 【总结】
     * <pre>
     * 0. 数据库的初始化版本必须是 1，数据库升级 version 必须与 model 对应起来，维护好文档；
     * 1. 新安装的App，不管是哪个数据库版本，都只会走 onCreate 方法
     *      => 需要在 onCreate() 方法中，确保数据库是最新的即可；
     * 2. 数据库升级时，只会走 onUpgrade() 方法，包括以下情形：
     *      a. v1 到 v2 - 执行 [v2] 升级
     *      b. v2 到 v3 - 执行 [v3] 升级
     *      c. v1 到 v4 - 需执行 [v2, v4] 之间闭区间的 SQL 升级
     * 3. onUpgrade()升级操作执行时，走的 case 代码分支，都是当前版本对应的最新数据库结构；
     *    使用 switch case 结构，执行区间操作 (oldVersion, newVersion]：
     *    <code>
     *     synchronized (MyDatabaseHelper.this) {
 *             for (int i = oldVersion + 1; i <= newVersion; i++) {  // (oldVersion, newVersion]
 *                 switch (i) {
 *                     case 2:
 *                         updateVersion2(db);
     *                 ...
     *      </code>
     * </p>
     * ===============
     * ================ 附：sqlite 升级原则
     * sqlite 操作原则：
     *  1. 升级只添加列，不修改列，不删除列
     *  2. 最好不修改表名；
     * </pre>
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("better", "onCreate: " + db.getVersion());
        // 所有表 model
        final Class<?>[] classes = new Class<?>[]{
                User.class, User2.class, Cat.class, AuthEntity.class
        };
        for (Class<?> aClass : classes) {
            DatabaseHelper.createTable(aClass, db);
        }
    }

    /**
     * 执行区间的每次升级
     * (oldVersion, newVersion]
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("better", "onUpgrade, old:" + oldVersion + ", new: " + newVersion);
        synchronized (MyDatabaseHelper.this) {
            for (int i = oldVersion + 1; i <= newVersion; i++) {  // (oldVersion, newVersion]
                switch (i) {
                    case 2:
                        updateVersion2(db);
                        break;
                    case 3:
                        updateVersion3(db);
                        break;
                    case 4:
                        updateVersion4(db);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void updateVersion2(SQLiteDatabase db) {
        // 版本2：新增 User 表新增 1个字段
        final String sql = "ALTER TABLE tb_user ADD COLUMN summary Text;";
        db.execSQL(sql);
    }

    private void updateVersion3(SQLiteDatabase db) {
        // 版本3：新增 User 表新增 1个字段
        final String sql2 = "ALTER TABLE tb_user ADD COLUMN sex INTEGER;";
        db.execSQL(sql2);
    }

    private void updateVersion4(SQLiteDatabase db) {
        // 版本4
        final String sql3 = "ALTER TABLE tb_user ADD COLUMN company Text;";
        db.execSQL(sql3);
    }

}
