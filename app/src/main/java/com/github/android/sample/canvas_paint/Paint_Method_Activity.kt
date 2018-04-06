package com.github.android.sample.canvas_paint

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.paint.*
import kotlinx.android.synthetic.main.activity_paint__method_.*

class Paint_Method_Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint__method_)

        setTitleFromIntent(intent)

        btn_paint_strokeCap.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintMethodView1(this))
        }

        btn_paint_strokeJoin.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintMethodView2(this))
        }

        btn_paint_corner_path_effect.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintMethodView3(this))
        }

        btn_paint_DashPathEffect.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintMethodView4(ctx = this))
        }

        btn_paint_Discrete.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintMethodView5(ctx = this))
        }

        btn_paint_path_dash_path.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintMethodView6(ctx = this))
        }

        btn_paint_compose_sum.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PaintMethodView7(ctx = this))
        }
    }
}
