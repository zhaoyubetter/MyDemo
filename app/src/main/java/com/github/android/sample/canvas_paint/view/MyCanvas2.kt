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
class MyCanvas2(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }

        val rect = RectF(300f, 10f, 500f, 100f)
        canvas.drawRect(rect, paint1)

        // canvas rotate ,画布顺时针旋转45度后
        canvas.rotate(30f)

        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.FILL
            color = Color.GREEN
        }
        canvas.drawRect(rect, paint2)
    }
}