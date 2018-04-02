package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/3/31.
 */
class MyCanvas1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }

        val rect = RectF(10f, 10f, 200f, 168f)
        canvas.drawRect(rect, paint1)

        // canvas平移
        canvas.translate(100f, 100f)

        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.GREEN
        }
        canvas.drawRect(rect, paint2)
    }
}