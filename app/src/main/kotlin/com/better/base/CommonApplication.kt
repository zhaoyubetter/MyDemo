package com.better.base

import android.app.Application
import com.better.base.lifecycle.CommonActivityLifeCycleCallback
import android.app.ActivityManager
import android.content.Context
import android.os.Process


/**
 * Created by zhaoyu on 2018/3/11.
 */
class CommonApplication : Application() {

    companion object {
        var ctx: Application? = null
        fun getContext(): Context {
            return ctx!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        ctx = this
        registerActivityLifecycleCallbacks(CommonActivityLifeCycleCallback())

        val pid = Process.myPid()
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcessInfo in am.runningAppProcesses) {
            if (appProcessInfo.pid == pid) {
                e2("app", appProcessInfo.processName)
                break
            }
        }
    }
}