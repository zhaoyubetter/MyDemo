package com.github.android.sample.canvas_paint

import android.os.Bundle
import android.view.ViewGroup
import android.widget.SeekBar
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.colorMatrix.*
import kotlinx.android.synthetic.main.activity_paint_color_matrix.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onSeekBarChangeListener
import org.jetbrains.anko.seekBar
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class PaintColorMatrixActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_color_matrix)
        setTitleFromIntent(intent)


        btn_color_matrix_1.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View1(this))
        }

        btn_color_matrix_2.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View2(this))
        }

        btn_color_matrix_3.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View3(this))
        }

        btn_color_matrix_4.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View4(this))
        }

        btn_color_matrix_5.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View5(this))
        }

        btn_color_matrix_6.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(ColorMatrix_View6(this))
        }

        btn_color_matrix_7.setOnClickListener {
            root_container.removeAllViews()
            val view7 = ColorMatrix_View7(root_container.context)
            with(root_container) {
                verticalLayout {
                    addView(view7)
                    seekBar {
                        max = 20
                        progress = 1
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }

                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                view7.setSaturation(progress.toFloat())
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
        }


        btn_color_matrix_8.setOnClickListener {
            root_container.removeAllViews()
            val view7 = ColorMatrix_View8(root_container.context)
            with(root_container) {
                verticalLayout {
                    addView(view7)
                    textView {
                        text = "红色"
                    }

                    fun change() {
                        view7.colorMatrix.setScale(
                                find<SeekBar>(android.R.id.text1).progress * 1.0f / 10,
                                find<SeekBar>(android.R.id.text2).progress * 1.0f / 10,
                                find<SeekBar>(android.R.id.tabs).progress * 1.0f / 10, 1f)
                        view7.postInvalidate()
                    }

                    seekBar {
                        id = android.R.id.text1
                        max = 20
                        progress = 10
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                change()
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)

                    textView {
                        text = "绿色"
                    }
                    seekBar {
                        id = android.R.id.text2
                        max = 20
                        progress = 10
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                change()
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)

                    textView {
                        text = "蓝色"
                    }
                    seekBar {
                        id = android.R.id.tabs
                        max = 20
                        progress = 10
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                change()
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
        } // 缩放

        btn_color_matrix_9.setOnClickListener {
            root_container.removeAllViews()
            val view7 = ColorMatrix_View8(root_container.context)
            with(root_container) {
                verticalLayout {
                    addView(view7)
                    textView {
                        text = "红色轴旋转"
                    }

                    seekBar {
                        id = android.R.id.text1
                        max = 360
                        progress = 180
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                view7.colorMatrix.setRotate(0, progress - 180 * .10f)
                                view7.postInvalidate()
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)

                    textView {
                        text = "绿色轴旋转"
                    }
                    seekBar {
                        id = android.R.id.text2
                        max = 20
                        progress = 10
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                view7.colorMatrix.setRotate(0, progress - 180 * .10f)
                                view7.postInvalidate()
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)

                    textView {
                        text = "蓝色轴旋转"
                    }
                    seekBar {
                        id = android.R.id.tabs
                        max = 20
                        progress = 10
                        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            }
                            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                                view7.colorMatrix.setRotate(0, progress - 180 * .10f)
                                view7.postInvalidate()
                            }
                        })
                    }.lparams(width = ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
        } // rotate

        btn_color_matrix_10.setOnClickListener {

        }
    }
}
