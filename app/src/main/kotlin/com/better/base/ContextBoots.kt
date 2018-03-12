package com.better.base

import android.content.Context
import android.util.Log
import android.widget.Toast
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

// ==== toast
inline fun Context.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, msg, duration).show()