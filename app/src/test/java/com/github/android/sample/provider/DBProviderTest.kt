package com.github.android.sample.provider

import android.content.Context
import android.support.test.InstrumentationRegistry
import com.github.android.sample.provider.db.database.DatabaseHelper
import org.junit.Before
import org.junit.Test

/**
 * Created by better On 2019-09-05.
 */
class DBProviderTest {

    lateinit var ctx: Context

    @Before
    fun before() {
        ctx = InstrumentationRegistry.getTargetContext()
    }

    @Test
    fun testCreateTable() {
        val helper = DatabaseHelper.getInstance()
        print(helper)
    }
}