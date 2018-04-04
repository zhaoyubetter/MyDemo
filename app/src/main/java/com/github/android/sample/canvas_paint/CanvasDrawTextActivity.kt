package com.github.android.sample.canvas_paint

import android.app.Activity
import android.graphics.Paint
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.drawText.*
import kotlinx.android.synthetic.main.activity_canvas_draw_text.*

class CanvasDrawTextActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_draw_text)
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }

        root_container.addView(MyDrawText_View1(this))

        btn_baseline_1.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View1(this))
        }

        btn_baseline_2.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View2(this))
        }

        // x 方向
        btn_x_left.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View3(this))
        }

        btn_x_center.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View3(this).apply { setAlign(Paint.Align.CENTER) })
        }

        btn_x_right.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View3(this).apply { setAlign(Paint.Align.RIGHT) })
        }

        btn_font_metrics.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View4(this))
        }

        btn_min_rect.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View5(this))
        }

        btn_draw_on_top.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View6(this))
        }

        btn_draw_on_center.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyDrawText_View7(this))
        }
    }
}
