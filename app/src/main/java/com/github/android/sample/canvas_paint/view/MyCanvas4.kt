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
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }
        canvas.drawRect(10f, 10f, 200f, 100f, paint1)

        // x倾斜60°，y不变
        canvas.skew(1.732f, 0f)
        paint1.color = Color.GREEN
        canvas.drawRect(10f, 10f, 200f, 100f, paint1)
    }
}