package com.github.android.sample.canvas_paint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.colorFilter.ColorFilterView1
import com.github.android.sample.canvas_paint.colorFilter.ColorFilterView2
import kotlinx.android.synthetic.main.activity_paint_color_filter.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class PaintColorFilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_color_filter)


        btn_color_filter_1.onClick {
            root_container.removeAllViews()
            root_container.addView(ColorFilterView1(this@PaintColorFilterActivity))
        }

        btn_color_filter_2.onClick {
            root_container.removeAllViews()
            root_container.addView(ColorFilterView2(this@PaintColorFilterActivity))
        }
    }
}
