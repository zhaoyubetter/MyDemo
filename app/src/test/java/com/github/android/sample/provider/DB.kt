package com.github.android.sample.provider

import android.content.Context
import android.support.test.InstrumentationRegistry
import com.better.base.d
import com.github.android.sample.R
import com.github.android.sample.provider.db.User
import com.github.android.sample.provider.db.database.DatabaseHelper
import org.junit.Before
import org.junit.Test

/**
 * @author zhaoyu1  2019-09-26
 * 你妹：
 * No instrumentation registered! Must run under a registering instrumentation.
 **/

class DB {
    lateinit var ctx: Context

    @Before
    fun before() {
        ctx = InstrumentationRegistry.getTargetContext()
    }

    @Test
    fun testCreateTable() {
        println("ok===")
        println(ctx.getString(R.string.app_name))
        val helper = DatabaseHelper.getInstance()
        d("" + helper.queryList(User::class.java, "age > ?", arrayOf("50"), null)?.size)
    }
}