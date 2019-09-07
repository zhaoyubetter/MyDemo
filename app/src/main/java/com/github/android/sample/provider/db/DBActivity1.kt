package com.github.android.sample.provider.db

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.d
import com.github.android.sample.R
import com.github.android.sample.provider.db.database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_db1.*

class DBActivity1 : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db1)

        val dbHelper = DatabaseHelper.getInstance()

        dbHelper.setOnDbUpgradeListener { db, oldVersion, newVersion ->
            d("old: $oldVersion, new:$newVersion")
        }

        btn_add.setOnClickListener {
            for (i in (0..100)) {
                dbHelper.insert(User(name = "better$i", age = 20 + i))
            }
        }

        btn_query.setOnClickListener {
            dbHelper.insert(User(name = "Chelsea", age = 0))
            d("" + dbHelper.queryCount(User::class.java))
        }

        btn_query2.setOnClickListener {
            d("" + dbHelper.query(User::class.java, "name=?", arrayOf("Chelsea"), null))
            d("" + dbHelper.queryList(User::class.java, "age > ?", arrayOf("50"), null)?.size)
        }
    }
}
