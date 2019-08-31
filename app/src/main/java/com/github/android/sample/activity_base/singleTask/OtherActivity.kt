package com.github.android.sample.activity_base.singleTask

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_single_task.*

class OtherActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_other)
        text.text = "${text.text} ${taskId}"

        setTitle("normal")
    }
}
