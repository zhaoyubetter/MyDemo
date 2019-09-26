package provider

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.better.base.CommonApplication
import com.better.base.d
import com.github.android.sample.R
import com.github.android.sample.provider.db.User
import com.github.android.sample.provider.db.database.DatabaseHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 设备化测试
 * Created by better On 2019-09-05.
 * 什么情况，总是提示这个
 * Client not ready yet.. Started running tests
 */
@RunWith(AndroidJUnit4::class)
class DBProviderTest {

    lateinit var ctx: Context

    @Before
    fun before() {
        val appContext = InstrumentationRegistry.getTargetContext()
        println(appContext)
        ctx = CommonApplication.getContext()
    }

    @Test
    fun testCreateTable() {
        println("ok===")
        println(ctx.getString(R.string.app_name))
        val helper = DatabaseHelper.getInstance()
        d("" + helper.queryList(User::class.java, "age > ?", arrayOf("50"), null)?.size)
    }
}