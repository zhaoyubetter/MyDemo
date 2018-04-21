package com.github.android.sample.canvas_paint.shader

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.better.base.w
import com.github.android.sample.R
import android.graphics.Shader
import android.graphics.LinearGradient
import android.widget.TextView
import org.jetbrains.anko.matchParent
import android.graphics.RadialGradient


class ShaderView11(ctx: Context, attributeSet: AttributeSet? = null) : TextView(ctx, attributeSet) {


    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var shade: RadialGradient? = null
    val paint = Paint().apply {
    }
    val radius = 400f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 双色
//        shade = RadialGradient(width / 2.toFloat(), height / 2.toFloat(),
//                radius, 0xffff0000.toInt(), 0xff00ff00.toInt(), Shader.TileMode.REPEAT)

        // 多色
        val colors = intArrayOf(-0x10000, -0xff0100, -0xffff01, -0x100)
        val stops = floatArrayOf(0f, 0.2f, 0.5f, 1f)
        shade = RadialGradient(width / 2.toFloat(), height / 2.toFloat(),
                100f, colors, stops, Shader.TileMode.REPEAT)


        paint.shader = shade
        canvas.drawCircle(200f, 200f,150f, paint)
    }

}