package com.github.android.sample.activity_base.singleTask

import android.content.Intent
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.toast
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_single_task.*

/**
 * SingleTaskActivity 是否在新的任务栈，需要配置一下：taskAffinity
 *
 */
class SingleTaskActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_task)

        button.setOnClickListener {
            startActivity(Intent(SingleTaskActivity@ this, OtherActivity::class.java).apply {
            })
        }

        text.text = "${text.text} ${taskId}"


        setTitle("SingleTask")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        toast("onNewIntent")
    }
}
