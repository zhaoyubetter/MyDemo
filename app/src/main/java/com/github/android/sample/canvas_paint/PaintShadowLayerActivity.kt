package com.github.android.sample.canvas_paint

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.shadow.ShadowView1
import com.github.android.sample.canvas_paint.shadow.ShadowView2
import com.github.android.sample.canvas_paint.shadow.ShadowView3
import kotlinx.android.synthetic.main.activity_paint_shadow_layer.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onSeekBarChangeListener
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class PaintShadowLayerActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_shadow_layer)
        setTitleFromIntent(intent)

        btn_layer_list.setOnClickListener {
            root_container.removeAllViews()
            val view = ShadowView1(this@PaintShadowLayerActivity)
            with(root_container) {
                verticalLayout {
                    addView(view)
                    textView {
                        text = "模糊半径"
                    }
                    seekBar {
                        max = 100
                        progress = 10
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                view.radius = progress.toFloat()
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)

                    textView {
                        text = "阴影x方向偏移"
                    }
                    seekBar {
                        max = 100
                        progress = 60
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                view.dx = progress.toFloat() - 50
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)

                    textView {
                        text = "阴影Y方向偏移"
                    }
                    seekBar {
                        max = 100
                        progress = 60
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                view.dy = progress.toFloat() - 50
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
        }

        btn_text_layer.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(TextView(this@PaintShadowLayerActivity).apply {
                setShadowLayer(10f, 10f, 10f, Color.GRAY)
                textColor = Color.BLACK
                text = "非常感谢启舰提供这么好的教程"
                textSize = 20f
            })
        }

        btn_mask_filter.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ShadowView2(this@PaintShadowLayerActivity))
        }

        btn_bitmap.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ShadowView3(this@PaintShadowLayerActivity))
        }
    }
}
