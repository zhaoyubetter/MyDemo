package com.github.android.sample.canvas_paint

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.path.PathView1
import com.github.android.sample.canvas_paint.path.PathView2
import kotlinx.android.synthetic.main.activity_canvas_path.*

class CanvasPathActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_path)
        setTitleFromIntent(intent)

        btn_path_quard.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PathView1(this))
        }

        btn_path_gesture.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(PathView2(this))
        }
    }
}
