package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu1 on 2018/3/26.
 */
class PaintCanvasView1(ctx: Context, attr: AttributeSet? = null) : View(ctx, attr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.FILL
            strokeWidth = 5f
            setShadowLayer(10f, 15f, 15f, Color.GREEN)   // 设置阴影
        }
        canvas.drawRGB(255, 255, 255)   // 画布背景
        canvas.drawCircle(200f, 200f, 150f, paint) // 画圆
        canvas.drawLine(0f, 0f, 100f, 10f, paint)

        // multi line
        val pts = floatArrayOf(200f, 350f, 230f, 400f, 250f, 450f, 300f, 500f)
        canvas.drawLines(pts, paint)

        // drawPoint
        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.GREEN
            style = Paint.Style.FILL
            strokeWidth = 20f
        }
        canvas.drawPoint(500f, 500f, paint2)
        val points = floatArrayOf(550f, 550f, 550f, 600f, 500f, 650f, 500f, 700f)
        // // 跳过2个数值，然后用后面6个点，来画poiner，也就是最后3个点，
// 如果count传入4，即最后一个不画
        canvas.drawPoints(points, 2, 6, paint2)

        // 圓角矩形
        val rectF = RectF(20f, 600f, 800f, 200f)
        canvas.drawRoundRect(rectF, 15f, 15f, paint)
    }
}