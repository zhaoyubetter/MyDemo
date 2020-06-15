package com.github.android.sample.activity_base.singleTask

import android.content.Intent
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.toast
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_single_task.*

/**
 * 这里按返回键会回到主task,而不是 SingleTask2Activity
 */
class SingleTask3Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_3task)


        text.text = "${text.text} ${taskId}"


        setTitle("SingleTask为配置Affinity")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        toast("onNewIntent")
    }
}
