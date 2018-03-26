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
class PaintCanvasView2(ctx: Context, attr: AttributeSet? = null) : View(ctx, attr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE//填充样式改为描边
            strokeWidth = 5f//设置画笔宽度
        }

        val rect = RectF(100f, 10f, 600f, 300f)
        canvas.drawRect(rect, paint)//画矩形
        paint.color = Color.GREEN//更改画笔颜色
        canvas.drawOval(rect, paint)//同一个矩形画椭圆
    }
}