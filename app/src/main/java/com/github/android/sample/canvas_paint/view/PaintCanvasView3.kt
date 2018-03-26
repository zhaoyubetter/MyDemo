package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * 弧
 * Created by zhaoyu1 on 2018/3/26.
 */
class PaintCanvasView3(ctx: Context, attr: AttributeSet? = null) : View(ctx, attr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE//填充样式改为描边
            strokeWidth = 2f//设置画笔宽度
        }

        val rect = RectF(20f, 10f, 600f, 300f)
        canvas.drawRect(rect, paint)
        paint.color = Color.GREEN
        paint.strokeWidth = 5f
        canvas.drawArc(rect, 0f, 90f, true, paint)
        canvas.drawArc(rect, 180f, 90f, false, paint)

        // 另一个
        val paint2 = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE//填充样式改为描边
            strokeWidth = 2f//设置画笔宽度
        }

        val rect2 = RectF(40f, 600f, 600f, 600f + 290f)
        canvas.drawRect(rect2, paint2)

        paint2.color = Color.GREEN
        paint2.strokeWidth = 5f
        paint2.style = Paint.Style.FILL
        canvas.drawArc(rect2, 0f, 90f, true, paint2)
        canvas.drawArc(rect2, 180f, 90f, false, paint2)
    }
}