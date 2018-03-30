package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu1 on 2018/3/30.
 */
class PaintFontView2(ctx: Context, attr: AttributeSet? = null) : View(ctx, attr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 38f
            color = Color.RED
            strokeWidth = 2f
            textAlign = Paint.Align.LEFT
            style = Paint.Style.FILL

            // 样式设置
            isFakeBoldText = true
            isUnderlineText = true
            isStrikeThruText = true
        }

        // 水平倾斜度为：0.25，右斜
        paint.textSkewX = -0.25f
        canvas.drawText("textSkewX -0.25f ==> Kotlin 重新学Android", 10f, 100f, paint)
        paint.textSkewX = 0.25f
        canvas.drawText("textSkewX 0.25f ==> Kotlin 重新学Android", 10f, 200f, paint)

        canvas.drawLine(0f, 235f, width.toFloat(), 235f, paint)

        // 水平拉伸
        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 38f
            color = Color.RED
            strokeWidth = 2f
            style = Paint.Style.FILL
        }

        // 正常绘制
        canvas.drawText("Kotlin 重新学Android", 10f, 300f, paint2)
        paint2.textScaleX = 2f  // 水平拉伸2倍
        canvas.drawText("Kotlin 重新学Android", 10f, 400f, paint2)

        // 对比
        paint2.textScaleX = 1f  //
        canvas.drawText("Android开发艺术探索", 10f, 500f, paint2)
        paint2.textScaleX = 2f
        paint2.color = Color.GREEN
        canvas.drawText("Android开发艺术探索", 10f, 500f, paint2)
    }
}