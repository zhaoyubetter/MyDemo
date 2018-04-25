package com.github.android.sample.widget.viewgroup

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R

class FlowLayoutActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_layout)
        setTitleFromIntent(intent)


    }
}
