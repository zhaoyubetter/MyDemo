package com.github.android.sample.canvas_paint.drawText

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu1 on 2018/4/3.
 */
class MyDrawText_View1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
            textSize = 38f
        }
        canvas.drawText("Android开发艺术探索", 0f, 10f, paint1)
    }
}