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
class MyCanvas3(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }
        canvas.drawCircle(300f, 300f, 148f, paint1)
        canvas.scale(0.5f,1f)
        //canvas.scale(0.5f,1f, 300f,300f)
        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.GREEN
        }
        canvas.drawCircle(300f, 300f, 148f, paint2)
    }
}