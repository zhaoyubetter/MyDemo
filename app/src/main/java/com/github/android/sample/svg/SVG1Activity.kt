package com.github.android.sample.svg

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R

class SVG1Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleFromIntent(intent)
        setContentView(R.layout.activity_svg1)
    }
}
