package com.github.android.sample.canvas_paint.drawText

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.better.base.e
import android.graphics.Paint.FontMetricsInt


/**
 * Created by zhaoyu1 on 2018/4/3.
 */
class MyDrawText_View5(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        textSize = 100f
        setTextAlign(Paint.Align.LEFT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val stringText = "Android g 开发艺术探索"

        val baselineX = 0f
        val baselineY = 200f

        // == 1. 画text所占的区域Rect
        val fm = paint.fontMetricsInt
        val top = baselineY + fm.top            // 加 baselineY
        val bottom = baselineY + fm.bottom
        val width = paint.measureText(stringText).toInt()
        val rect = RectF(baselineX, top, baselineX + width, bottom)

        paint.color = Color.GREEN
        canvas.drawRect(rect, paint)

        // == 2.写文字
        paint.color = Color.BLACK
        canvas.drawText(stringText, baselineX, baselineY, paint)

        // == 3.画文字对应的最小矩形
        val minRect = Rect()
        paint.getTextBounds(stringText, 0, stringText.length, minRect)
        minRect.top = baselineY.toInt() + minRect.top
        minRect.bottom = baselineY.toInt() + minRect.bottom
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        canvas.drawRect(minRect, paint)
    }
}