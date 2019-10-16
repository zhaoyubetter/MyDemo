package com.github.android.sample.ipc.subthread

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.android.sample.R

import org.jetbrains.anko.toast

class SubThreadResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipc_sub_thread_result)
        // 测试结果，跟哪里启动Activity没有关系
        toast(intent.getStringExtra("mydata") + ", 线程名：" + Thread.currentThread().name)

    }

}
