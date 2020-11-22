package com.better.base

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.util.Log
import android.widget.Toast
import com.better.base.model.SampleItem
import com.yu.bundles.log.MaeLog
import java.lang.Exception

/**
 * Created by zhaoyu on 2018/3/11.
 */
// ============= 全局 log 控制 参考 cz
inline fun <reified T> T.v(log: String) = Log.v(T::class.java.name, log)

inline fun <reified T> T.i(log: String) = Log.i(T::class.java.name, log)
inline fun <reified T> T.d(log: String) = Log.d(T::class.java.name, log)
inline fun <reified T> T.w(log: String) = Log.w(T::class.java.name, log)
inline fun <reified T> T.e(log: String) = MaeLog.e(T::class.java.name, log)
inline fun <reified T> T.e(tag:String, log: String) = MaeLog.e(tag, log)
inline fun <reified T> T.e(e: Exception) = Log.e(T::class.java.name, null, e)

inline fun <reified T> T.e2(tag: String = T::class.java.name, log: String) = Log.e(tag, log)

inline fun <reified T> T.isNotNull() = null != this

inline fun <reified T> T.isNull() = !isNotNull()

// ==== toast
inline fun <T : Any> Context.toast(msg: T?, duration: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, msg?.toString(), duration).show()

// ==== Activity
inline fun Activity.isValid() = null != this && !this.isFinishing

// == 获取进程名
inline fun Context.getProcessName():String {
    val pid = Process.myPid()
    val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (appProcessInfo in am.runningAppProcesses) {
        if (appProcessInfo.pid == pid) {
            return appProcessInfo.processName
        }
    }
    return ""
}

/* ====== 项目中使用 ==========*/
// === for toolbarActivity
inline fun ToolbarActivity.setTitleFromIntent(intent: Intent) {
    intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
        supportActionBar?.title = it.title
        supportActionBar?.subtitle = it.desc
    }
}