package com.github.android.sample.canvas_paint.drawText

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu1 on 2018/4/3.
 */
class MyDrawText_View6(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            textSize = 38f
            setTextAlign(Paint.Align.LEFT)
        }

        val stringText = "Android g 开发艺术探索"

        val top = 200f
        val baselineX = 0f


        // == 画top线
        paint.color = Color.BLUE
        canvas.drawLine(baselineX, top, width.toFloat(), top, paint)

        // == 计算baseline，并画出baseline
        val fontMetrics = paint.fontMetrics
        val baseLineY = top - fontMetrics.top

        paint.color = Color.RED
        canvas.drawLine(baselineX, baseLineY, width.toFloat(), baseLineY, paint)

        // == 画文字
        paint.color = Color.GREEN
        canvas.drawText(stringText, baselineX, baseLineY, paint)

    }
}