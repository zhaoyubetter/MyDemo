package com.github.android.sample.canvas_paint

import android.graphics.drawable.DrawableWrapper
import android.os.Bundle
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.colorFilter.ColorFilterView1
import com.github.android.sample.canvas_paint.colorFilter.ColorFilterView2
import com.github.android.sample.canvas_paint.colorFilter.ColorFilterView3
import kotlinx.android.synthetic.main.activity_paint_color_filter.*
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class PaintColorFilterActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_color_filter)
        setTitleFromIntent(intent)

        btn_color_filter_1.onClick {
            root_container.removeAllViews()
            root_container.addView(ColorFilterView1(this@PaintColorFilterActivity))
        }

        btn_color_filter_2.onClick {
            root_container.removeAllViews()
            root_container.addView(ColorFilterView2(this@PaintColorFilterActivity))
        }

        btn_color_filter_3.setOnClickListener {
            root_container.removeAllViews()
            with(root_container) {
                val drawable = resources.getDrawable(R.mipmap.icon_map)
                linearLayout {
                    imageView {
                        setImageDrawable(drawable)
                    }.lparams(width = ViewGroup.LayoutParams.WRAP_CONTENT)
                    imageView {
                        resources.getDrawable(R.mipmap.icon_map).let {
                            it.setTint(0x00ff00)
                            setImageDrawable(it)
                        }
                    }.lparams()
                }
            }
        }
    }
}
