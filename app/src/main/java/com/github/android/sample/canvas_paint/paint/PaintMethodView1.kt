package com.github.android.sample.canvas_paint.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/4/6.
 */
class PaintMethodView1(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 80f
            color = Color.GREEN
            style = Paint.Style.STROKE
        }

        paint.strokeCap = Paint.Cap.BUTT        // 无线帽
        canvas.drawLine(100f, 200f, 400f, 200f, paint)

        paint.strokeCap = Paint.Cap.SQUARE      // 方形
        canvas.drawLine(100f, 400f, 400f, 400f, paint)

        paint.strokeCap = Paint.Cap.ROUND       // 圆形
        canvas.drawLine(100f, 600f, 400f, 600f, paint)

        paint.strokeWidth = 2f
        paint.color = Color.RED
        canvas.drawLine(100f, 0f, 100f, height.toFloat(), paint)

    }
}