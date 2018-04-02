package com.better.base

import android.app.Application
import com.better.base.lifecycle.CommonActivityLifeCycleCallback

/**
 * Created by zhaoyu on 2018/3/11.
 */
class CommonApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(CommonActivityLifeCycleCallback())
    }
}