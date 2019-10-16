package com.github.android.sample.canvas_paint

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.colorFilter.ColorFilterView1
import com.github.android.sample.canvas_paint.colorFilter.ColorFilterView2
import com.github.android.sample.canvas_paint.colorFilter.ColorFilterView3
import kotlinx.android.synthetic.main.activity_paint_color_filter.*
import org.jetbrains.anko.imageView
import org.jetbrains.anko.linearLayout

class PaintColorFilterActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_color_filter)
        setTitleFromIntent(intent)

        btn_color_filter_1.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorFilterView1(this@PaintColorFilterActivity))
        }

        btn_color_filter_2.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorFilterView2(this@PaintColorFilterActivity))
        }

        btn_color_filter_3.setOnClickListener {
            root_container.removeAllViews()
            with(root_container) {
                val drawable = resources.getDrawable(R.mipmap.icon_map).mutate()
                linearLayout {
                    imageView {
                        setImageDrawable(drawable)
                    }.lparams(width = ViewGroup.LayoutParams.WRAP_CONTENT)
                    imageView {
                        resources.getDrawable(R.mipmap.icon_map).mutate().let {
                            DrawableCompat.setTint(it, Color.RED)
                            setImageDrawable(it)
                        }
                    }.lparams()

                    imageView {
                        resources.getDrawable(R.mipmap.icon_map).mutate().let {
                            DrawableCompat.setTint(it, Color.GREEN)
                            setImageDrawable(it)
                        }
                    }.lparams()

                    imageView {
                        resources.getDrawable(R.mipmap.icon_map).mutate().let {
                            DrawableCompat.setTint(it, Color.BLACK)
                            setImageDrawable(it)
                        }
                    }.lparams()

                    imageView {
                        resources.getDrawable(R.mipmap.icon_map).mutate().let {
                            DrawableCompat.setTint(it, Color.GRAY)
                            setImageDrawable(it)
                        }
                    }.lparams()

                    imageView {
                        resources.getDrawable(R.mipmap.icon_map).mutate().let {
                            DrawableCompat.setTint(it, Color.YELLOW)
                            setImageDrawable(it)
                        }
                    }.lparams()
                }
            }
        }

        btn_color_filter_4.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorFilterView3(this@PaintColorFilterActivity))
        }
    }
}
