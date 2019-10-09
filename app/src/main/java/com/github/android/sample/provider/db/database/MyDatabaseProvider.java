package com.github.android.sample.provider.db.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author zhaoyu1  2019-09-27
 **/
public class MyDatabaseProvider extends DatabaseProvider {

    @Override
    public SQLiteOpenHelper getSQLiteHelper(Context context, String dbName) {
        return new MyDatabase(context, dbName);
    }

    @Override
    public String getInitDbName() {
        return "mydb.db";       // 初始化的dbName
    }
}
