package com.github.android.sample.canvas_paint

import android.app.Activity
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.view.PaintFontView3_OnPath
import kotlinx.android.synthetic.main.activity_paint_canvas__base2_.*

class PaintCanvas_Base2_Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_canvas__base2_)
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }

        // drawOnPath
        btn_draw_drawTextPath.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintFontView3_OnPath(this))
        }

        // typeface
        btn_draw_font_typeface.setOnClickListener {

        }
    }
}
