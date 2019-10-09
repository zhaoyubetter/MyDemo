package com.github.android.sample.provider.db.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author :Created by cz, better
 * @date 2019-06-21 16:29
 * 数据库实现对象
 */
public class MyDatabase extends SQLiteOpenHelper {
    /**
     * 当前数据库版本
     */
    private static final int CURRENT_VERSION = 2;
    private Context context;

    public MyDatabase(Context context, String name) {
        super(context, name, null, CURRENT_VERSION);
        context = context.getApplicationContext();
    }

    /**
     * 数据库第一次创建的时候调用
     * getWritableDatabase()的时候才会创建数据库
     * SQLiteDatabase方法何时执行？
     * <p>
     * 1、SQLiteOpenHelper会自动检测数据库文件是否存在。如果存在，会打开这个数据库，
     * 在这种情况下就不会调用onCreate()方法。如果数据库文件不存在，SQLiteOpenHelper首先会创建一个数据库文件，
     * 然后打开这个数据库，最后调用onCreate()方法。因此，onCreate()方法一般用来在新创建的数据库中建立表、视图等数据库组建。
     * 也就是说oncreate()方法在数据库文件第一次创建时调用。
     * <p>
     * 2、如果数据库文件不存在，只有oncreate()被调用（该方法在创建数据库时被调用一次）。
     * 如果数据库文件存在，会调用onupdate()方法升级数据库，并更新版本号。
     *
     * 问题是：
     * 如果我们卸载了程序，然后重装安装，此时数据库不存在，会执行onCreate方法，但假设测试 version 已经大于1了，
     * 也就是创建的数据库表就是最新的。
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("better", "onCreate: " + db.getVersion());
    }

    /**
     * 执行数据库操作时，此方法才走
     * 由于我们的数据库是在使用时才创建，如果用户卸载重装时，数据表都是最新的结构了。
     * 这点要额外注意。
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("better", "onUpgrade, old:" + oldVersion + ", new: " + newVersion);
        switch (newVersion) {
            case 2:
                break;
            default:
                break;
        }
        try {
            DatabaseHelper instance = DatabaseHelper.getInstance(context);
            //升级数据库版本
            instance.onUpgrade(db, oldVersion, newVersion);
        } finally {
            //设置数据库新的版本
            db.setVersion(newVersion);
        }
    }

}
