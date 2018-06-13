package com.better.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.better.base.model.SampleItem
import java.lang.Exception

/**
 * Created by zhaoyu on 2018/3/11.
 */
// ============= 全局 log 控制 参考 cz
inline fun <reified T> T.v(log: String) = Log.v(T::class.java.name, log)

inline fun <reified T> T.i(log: String) = Log.i(T::class.java.name, log)
inline fun <reified T> T.d(log: String) = Log.d(T::class.java.name, log)
inline fun <reified T> T.w(log: String) = Log.w(T::class.java.name, log)
inline fun <reified T> T.e(log: String) = Log.e(T::class.java.name, log)
inline fun <reified T> T.e(e: Exception) = Log.e(T::class.java.name, null, e)

inline fun <reified T> T.e2(tag: String = T::class.java.name, log: String) = Log.e(tag, log)

// ==== toast
inline fun Context.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, msg, duration).show()

/* ====== 项目中使用 ==========*/
// === for toolbarActivity
inline fun ToolbarActivity.setTitleFromIntent(intent: Intent) {
    intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
        supportActionBar?.title = it.title
        supportActionBar?.subtitle = it.desc
    }
}