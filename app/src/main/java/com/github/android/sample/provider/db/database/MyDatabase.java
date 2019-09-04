package com.github.android.sample.provider.db.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * @author :Created by cz
 * @date 2019-06-21 16:29
 * 数据库实现对象
 */
public class MyDatabase extends SQLiteOpenHelper {
    /**
     * 默认数据库版本
     */
    private static final int DEFAULT_VERSION = 1;

    public MyDatabase(Context context,String name) {
        super(context, name, null, DEFAULT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //升级版本
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance();
        int databaseVersion = databaseHelper.getDatabaseVersion();
        onUpgrade(db, db.getVersion(), databaseVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            DatabaseHelper instance = DatabaseHelper.getInstance();
            //升级数据库版本
            instance.onUpgrade(db,oldVersion,newVersion);
        } finally {
            //设置数据库新的版本
            db.setVersion(newVersion);
        }
    }

}
