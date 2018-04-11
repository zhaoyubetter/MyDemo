package com.github.android.sample.canvas_paint

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.xfermode.XfermodeView1
import kotlinx.android.synthetic.main.activity_paint_xfermode.*

class PaintXfermodeActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_xfermode)
        setTitleFromIntent(intent)

        root_container.removeAllViews()
        root_container.addView(XfermodeView1(this))
    }
}
