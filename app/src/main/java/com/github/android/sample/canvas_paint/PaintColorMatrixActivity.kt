package com.github.android.sample.canvas_paint

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.colorMatrix.*
import kotlinx.android.synthetic.main.activity_paint_color_matrix.*

class PaintColorMatrixActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_color_matrix)
        setTitleFromIntent(intent)


        btn_color_matrix_1.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View1(this))
        }

        btn_color_matrix_2.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View2(this))
        }

        btn_color_matrix_3.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View3(this))
        }

        btn_color_matrix_4.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View4(this))
        }

        btn_color_matrix_5.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View5(this))
        }

        btn_color_matrix_6.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View6(this))
        }
    }
}
