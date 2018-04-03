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
class MyDrawText_View2(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.FILL
            color = Color.RED
            textSize = 48f
        }

        val baselineX = 0f
        val baselineY = 200f
        // 画基线
        canvas.drawLine(baselineX, baselineY, width.toFloat(), baselineY, paint1)
        paint1.color = Color.GREEN
        canvas.drawText("Android开发艺术探索,fffggg", baselineX, baselineY, paint1)
    }
}