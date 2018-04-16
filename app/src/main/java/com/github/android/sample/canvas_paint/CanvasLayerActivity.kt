package com.github.android.sample.canvas_paint

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.layer.LayerView1
import kotlinx.android.synthetic.main.activity_canvas_layer.*

class CanvasLayerActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_layer)
        setTitleFromIntent(intent)

        // 尝试下整个window设置成透明时的src_IN效果
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        root_container.removeAllViews()
        root_container.addView(LayerView1(this))
    }
}
