package com.github.android.sample.ipc.sample

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Process
import com.better.base.e2
import com.better.base.getProcessName
import com.better.base.toast
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_ipctest1.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class IPCTest1Activity : AppCompatActivity() {

    companion object {
        var data: String = "currentData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipctest1)

        data = intent?.getStringExtra("setData") ?: "currentData"

        toast(data)

        val pid = Process.myPid()
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcessInfo in am.runningAppProcesses) {
            if (appProcessInfo.pid == pid) {
                e2("app", appProcessInfo.processName)
                break
            }
        }

        txt_result.text = "我运行在：${getProcessName()}"

        // use json transform
        btn_close.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra("data",MyData("better", 30).toString()))
            finish()
        }
    }

    private data class MyData(val name: String, val age: Int) {
        override fun toString() = """{"name":"$name","age":"$age"}"""
    }
}
