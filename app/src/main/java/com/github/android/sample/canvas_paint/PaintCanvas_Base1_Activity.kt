package com.github.android.sample.canvas_paint

import android.app.Activity
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.view.*
import kotlinx.android.synthetic.main.activity_paint_canvas__base1.*

class PaintCanvas_Base1_Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_canvas__base1)
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }

        // 添加
        root_container.addView(PaintCanvasView1(this))

        // 椭圆
        btn_draw_oval.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintCanvasView2(this))
        }

        // 弧
        btn_draw_arc.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintCanvasView3(this))
        }

        // path
        btn_draw_path.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintCanvasView4(this))
        }

        // 文字1
        btn_draw_font_style1.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintFontView1(this))
        }

        // 文字2
        btn_draw_font_style2.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintFontView2(this))
        }
    }
}
