package com.github.android.sample.provider.db

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.d
import com.better.base.e
import com.github.android.sample.R
import com.github.android.sample.provider.db.database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_db1.*

class DBActivity1 : ToolbarActivity() {

    val dbHelper = DatabaseHelper.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_db1)

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

        // 测试
        btn_test.setOnClickListener {
            //            testNull()
            testUnionPrimaryKey()
        }

        // 原生查询
        btn_rawQuery.setOnClickListener {
            val cursor = dbHelper.rawQuery("select * from tb_user", null)
            e("user count: " + cursor.count)
            cursor.close()
        }
    }

    private fun testUnionPrimaryKey() {
        var entity = AuthEntity().apply {
            id = 1
            appId = "appId1"
            scope = "scope1"
            permission = "permission1"
            title = "title1"
            description = "description1"
            state = 0
        }
        dbHelper.insert(entity)
        e("" + dbHelper.queryList(AuthEntity::class.java).size)

        entity = AuthEntity().apply {
            appId = "appId1"
            scope = "scope1"
            permission = "脑壳痛"

        }
        dbHelper.insert(entity)
        e("" + dbHelper.queryList(AuthEntity::class.java).size)
    }

    // 测试null值
    private fun testNull() {
//        dbHelper.truncate(Cat::class.java)
        // 批量添加
        val list = mutableListOf<Cat>()
        for (i in (1..100)) {
            list.add(Cat(i))
        }
        dbHelper.bulkInsert(list)
        e("" + dbHelper.queryList(Cat::class.java)?.size)
        e("" + dbHelper.queryList(Cat::class.java, "number > ?", arrayOf("50"), "number desc")?.size)
    }
}
