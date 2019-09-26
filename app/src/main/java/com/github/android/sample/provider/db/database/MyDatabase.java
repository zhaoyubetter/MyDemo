package com.github.android.sample.provider.db.database;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author :Created by cz
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
     *
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
