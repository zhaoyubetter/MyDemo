package com.github.android.sample.ipc.subthread

import android.content.Intent
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_ipc_thread_.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import kotlin.concurrent.thread

/**
 * 子线程启动Activity
 * @author zhaoyu1  2019/4/28
 **/
class SubThreadHandleActivity: ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleFromIntent(intent)
        setContentView(R.layout.activity_ipc_thread_)

        btn_test.onClick {
            thread {
                startActivity(Intent(applicationContext, SubThreadResultActivity::class.java).apply {
                    putExtra("mydata", "better")
                })
            }
        }
    }
}