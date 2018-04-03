package com.github.android.sample.canvas_paint.drawText

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.better.base.e

/**
 * Created by zhaoyu1 on 2018/4/3.
 */
class MyDrawText_View3(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        textSize = 48f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val stringText = "Android开发艺术探索,fffggg"


        // 平移到中间
        canvas.translate(width / 4.toFloat(), height / 2.toFloat())
        paint1.color = Color.GRAY
        paint1.style = Paint.Style.STROKE
        val rect = Rect()
        paint1.getTextBounds(stringText, 0, stringText.length, rect)
        // 画文字所在的矩形
        canvas.drawRect(0f, 0f, rect.width().toFloat(), rect.height().toFloat(), paint1)

        val baseLineX = -width / 2.toFloat()
        val baselineY = rect.height().toFloat()
        // 画基线
        paint1.color = Color.RED
        canvas.drawLine(baseLineX, baselineY, width.toFloat(), baselineY, paint1)

        // 画文字
        paint1.color = Color.GREEN
        paint1.style = Paint.Style.FILL
        canvas.drawText(stringText, 0f, baselineY, paint1)
    }

    fun setAlign(algin: Paint.Align) {
        paint1.textAlign = algin
        invalidate()
    }
}