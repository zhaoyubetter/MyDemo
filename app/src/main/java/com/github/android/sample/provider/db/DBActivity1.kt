package com.github.android.sample.provider.db

import android.os.Bundle
import com.better.base.CommonApplication
import com.better.base.ToolbarActivity
import com.better.base.d
import com.better.base.e
import com.github.android.sample.R
import com.github.android.sample.provider.db.database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_db1.*

class DBActivity1 : ToolbarActivity() {

    val dbHelper = DatabaseHelper.getInstance(CommonApplication.getContext())

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

        // call方法
        btn_call.setOnClickListener {
            val bundle = dbHelper.resetDatabase("better.db")
        }

        // like 操作
        btn_like.setOnClickListener {
            var entity = AuthEntity().apply {
                id = 1
                appId = "appId2"
                scope = "scope2"
                permission = "other"
                title = "title1"
                description = "description1"
                state = 0
            }
            dbHelper.insert(entity)

            dbHelper.queryList(AuthEntity::class.java, "permission like ?", arrayOf("oth%"), null)
        }

        // 子线程调用 ：所有的数据库和文件操作 都是block的主线程
        // 如果耗时请使用子线程操作；
        /* contentProvider 对应的 query 方法也会在调用方的子线程中运行
         * 如果contentProvider 配置了 process，则运行在binder线程中 */
        btn_thread.setOnClickListener {
            Thread {
                d("" + dbHelper.queryCount(User::class.java))
            }.start()
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
