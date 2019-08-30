package com.github.android.sample.canvas_paint

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.layer.*
import kotlinx.android.synthetic.main.activity_canvas_layer.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class CanvasLayerActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_layer)
        setTitleFromIntent(intent)


        btn_layer.setOnClickListener {
            // 尝试下整个window设置成透明时的src_IN效果
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            root_container.removeAllViews()
            root_container.addView(LayerView1(this@CanvasLayerActivity))
        }


        btn_save_layer_1.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(LayerView2(this@CanvasLayerActivity))
        }


        btn_save_layer_2.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(LayerView3(this@CanvasLayerActivity))
        }

        btn_save_layer_3.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(LayerView4(this@CanvasLayerActivity))
        }

        btn_save_layer_4.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(LayerView5(this@CanvasLayerActivity))
        }

    }
}
