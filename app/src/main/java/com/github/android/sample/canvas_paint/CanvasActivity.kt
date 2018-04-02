package com.github.android.sample.canvas_paint

import android.app.Activity
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.view.*
import kotlinx.android.synthetic.main.activity_canvas.*

class CanvasActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }
        root_container.addView(MyCanvas1(this))

        btn_translate.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyCanvas1(this))
        }

        btn_rotate.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyCanvas2(this))
        }

        btn_scale.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyCanvas3(this))
        }

        btn_skew.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyCanvas4(this))
        }

        btn_clip.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyCanvas5(this))
        }

        btn_save.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyCanvas6(this))
        }

        btn_layer.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyCanvas7Layer(this))
        }
    }
}
