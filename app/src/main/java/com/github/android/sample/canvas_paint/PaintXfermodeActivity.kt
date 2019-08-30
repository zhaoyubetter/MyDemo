package com.github.android.sample.canvas_paint

import android.graphics.Color
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.xfermode.*
import kotlinx.android.synthetic.main.activity_paint_xfermode.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk25.coroutines.onClick

class PaintXfermodeActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_xfermode)
        setTitleFromIntent(intent)


        root_container.addView(XfermodeView1(this))
        btn_18_mode.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(XfermodeView1(this@PaintXfermodeActivity))
        }

        btn_light.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(XfermodeView2(this@PaintXfermodeActivity))
        }

        btn_src.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(XfermodeView3(this@PaintXfermodeActivity))
        }

        btn_src_out.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(XfermodeView4(this@PaintXfermodeActivity))
        }

        // dst
        btn_DST_IN.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(XfermodeView5(this@PaintXfermodeActivity))
        }

        btn_DST_IN_2.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(XfermodeView6(this@PaintXfermodeActivity))
        }
    }
}
