package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/3/31.
 */
class MyCanvas4(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 5f
            style = Paint.Style.STROKE
            color = Color.BLACK
        }

        canvas.drawLine(0f, height / 2.toFloat(), width.toFloat(), height / 2.toFloat(), paint1)
        canvas.drawLine(width / 2.toFloat(), 0f, width / 2.toFloat(), height.toFloat(), paint1)

        canvas.translate(width / 2.toFloat(), height / 2.toFloat())

        paint1.color = Color.RED
        canvas.drawRect(0f, 0f, 200f, 100f, paint1)

        // x倾斜45，y不变
        //canvas.skew(1f, 0f)     // x方向45度错切
        // canvas.skew(-1f,0f)       // x方向-45度错切
//        canvas.skew(0f,1f)  // y方向斜切45
        canvas.skew(Math.tan(30.toDouble()).toFloat(), 0f)

        paint1.color = Color.BLACK
        canvas.drawRect(0f, 0f, 200f, 100f, paint1)
    }
}