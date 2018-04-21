package com.github.android.sample.canvas_paint.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.better.base.w
import com.github.android.sample.R
import android.graphics.Shader
import android.graphics.LinearGradient
import org.jetbrains.anko.matchParent


class ShaderView8(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {


    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 65f
        }

        // 多个颜色
        val colors = intArrayOf(-0x10000, -0xff0100, -0xffff01, -0x100, -0xff0001)
        val pos = floatArrayOf(0f, 0.2f, 0.4f, 0.6f, 1.0f)  // 20%，20%，20%，20%，40%
        val multiGradient = LinearGradient(0f, 0f, width / 2.toFloat(), height / 2.toFloat(), colors, pos, Shader.TileMode.REPEAT)
        paint.shader = multiGradient
        // 画文字
        canvas.drawText("非常感谢启舰，Thanks！", 100f, 500f, paint)
    }
}