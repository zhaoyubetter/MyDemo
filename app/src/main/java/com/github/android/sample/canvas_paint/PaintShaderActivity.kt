package com.github.android.sample.canvas_paint

import android.graphics.Color
import android.graphics.Shader
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout.HORIZONTAL
import android.widget.RadioGroup
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.shader.ShaderView1
import com.github.android.sample.canvas_paint.shader.ShaderView2
import com.github.android.sample.canvas_paint.shader.ShaderView3
import com.github.android.sample.canvas_paint.shader.ShaderView4
import kotlinx.android.synthetic.main.activity_keyframe.view.*
import kotlinx.android.synthetic.main.activity_paint_shader.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * shader着色器
 */
class PaintShaderActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_shader)
        setTitleFromIntent(intent)


        root_container.removeAllViews()
        root_container.addView(ShaderView1(this))

        btn_shader1.onClick {
            root_container.removeAllViews()
            val shaderView = ShaderView1(this@PaintShaderActivity)
            with(root_container) {
                verticalLayout {
                    addView(shaderView)
                    // 3个radioButton
                    radioGroup {
                        orientation = RadioGroup.HORIZONTAL
                        radioButton {
                            id = android.R.id.text1
                            text = "TileMode.Repeat"
                        }
                        radioButton {
                            id = android.R.id.text2
                            text = "TileMode.Clamp"
                        }
                        radioButton {
                            id = android.R.id.icon
                            text = "TileMode.Mirror"
                        }
                        setOnCheckedChangeListener { _, checkedId ->
                            toast("$checkedId")
                            when (checkedId) {
                                android.R.id.text1 -> shaderView.setTileMode(Shader.TileMode.REPEAT)
                                android.R.id.text2 -> shaderView.setTileMode(Shader.TileMode.CLAMP)
                                android.R.id.icon -> shaderView.setTileMode(Shader.TileMode.MIRROR)
                            }
                        }
                    }.lparams(width = android.view.ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
        }


        btn_shader2.onClick {
            root_container.removeAllViews()
            root_container.addView(ShaderView2(this@PaintShaderActivity))
        }

        btn_shader3.onClick {
            root_container.removeAllViews()
            root_container.addView(ShaderView3(this@PaintShaderActivity))
        }

        btn_shader4.onClick {
            root_container.removeAllViews()
            root_container.addView(ShaderView4(this@PaintShaderActivity))
        }
    }
}
