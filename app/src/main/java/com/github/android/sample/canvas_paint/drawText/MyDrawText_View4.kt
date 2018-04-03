package com.github.android.sample.canvas_paint.drawText

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.better.base.e

/**
 * Created by zhaoyu1 on 2018/4/3.
 */
class MyDrawText_View4(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        textSize = 100f
        setTextAlign(Paint.Align.LEFT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val stringText = "Android g 开发艺术探索"

        val baselineX = 0f
        val baselineY = 200f

        // 画文字
        canvas.drawText(stringText, baselineX, baselineY, paint1)

        // 计算各线在位置
        val fontMetrics = paint1.fontMetrics
        val top = baselineY + fontMetrics.top
        val ascent = baselineY + fontMetrics.ascent
        val descent = baselineY + fontMetrics.descent
        val bottom = baselineY + fontMetrics.bottom

        // top
        paint1.color = Color.BLUE
        canvas.drawLine(baselineX, top, width.toFloat(), top, paint1)
        // ascent
        paint1.color = Color.GREEN
        canvas.drawLine(baselineX, ascent, width.toFloat(), ascent, paint1)
        //画基线
        paint1.color = Color.RED
        canvas.drawLine(baselineX, baselineY, width.toFloat(), baselineY, paint1)
        // descent
        paint1.color = Color.BLACK
        canvas.drawLine(baselineX, descent, width.toFloat(), descent, paint1)
        // bottom
        paint1.color = Color.MAGENTA
        canvas.drawLine(baselineX, bottom, width.toFloat(), bottom, paint1)
    }
}