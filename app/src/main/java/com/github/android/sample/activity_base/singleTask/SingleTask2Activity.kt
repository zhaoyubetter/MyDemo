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
class SingleTask2Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_2task)

        // 这里启动3，3运行在主Task, 3 返回键将会到 SingleTaskMainTestActivity;
        // 如何解决了呢, startActivityForResult
        button.setOnClickListener {
//            startActivity(Intent(SingleTaskActivity@ this, SingleTask3Activity::class.java).apply {})
            startActivityForResult(Intent(SingleTaskActivity@ this, SingleTask3Activity::class.java).apply {}, 22)
        }

        text.text = "${text.text} ${taskId}"


        setTitle("SingleTask启动SingleTask")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        toast("onNewIntent")
    }
}
