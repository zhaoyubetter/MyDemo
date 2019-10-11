package com.github.android.sample.provider.db

import android.os.Bundle
import android.os.SystemClock
import com.better.base.CommonApplication
import com.better.base.ToolbarActivity
import com.better.base.d
import com.better.base.e
import com.github.android.sample.R
import com.github.android.sample.provider.db.database.DatabaseHelper
import kotlinx.android.synthetic.main.activity_db1.*
import java.util.*

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
         * 但如果contentProvider 配置了 process，则运行在binder线程中
         * 结论：contentProvider 耗时操作应该使用子线程,最好db操作都使用子线程
         * */
        btn_thread.setOnClickListener {
            Thread {
                d("" + dbHelper.queryCount(User::class.java))
            }.start()
        }

        // 多线程访问DB，db close 问题
        btn_db_close.setOnClickListener {
            doDBCloseWork()
        }
    }

    // 多线程访问DB，db close 问题
    // 目前 db 没有关闭
    // 参考：https://www.cnblogs.com/yuanhao-1999/p/11629609.html
    //      https://blog.csdn.net/qq_25412055/article/details/52414420
    /*
        总结：
        1. sqlite helper 对象必须使用单例模式，在 contentProvider 中就是单例可以放心使用；
        2. 如果我们想再不同的线程中，对数据库进行包括读写操作在内的任何使用，我们就必须得确保，我们使用的是同一个的连接；
        3. 我们需要保证，当没有人使用 SQLiteHelper 时，再将其断开连接，即 执行 db.close();
        4. 如果不断开db链接，会有：Leak found
            Caused by: java.lang.IllegalStateException: SQLiteDatabase created and never closed
        5. sqlite 同一时间只能进行一个写操作，需要保证db单例；
        6. 注意：尽量不要在多线程环境下操作数据库；
     */
    private fun doDBCloseWork() {
        (0..10).forEach { _ ->
            // 1. contentProvider 在独立进程中，报错如下：
            //   java.lang.IllegalStateException: Cannot perform this operation because the connection pool has been closed.
            // 2. contentProvider 在主进程中，报错如下：
            //   java.lang.IllegalStateException: Cannot perform this operation because the connection pool has been closed.
            Thread {
                var entity = AuthEntity().apply {
                    id = 1
                    appId = "appId2"
                    scope = "scope2"
                    permission = "other"
                    title = "title1"
                    description = "description1"
                    state = 0
                }
                SystemClock.sleep(Random().nextInt(200).toLong())
                d(Thread.currentThread().name)
                dbHelper.insert(entity)
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
