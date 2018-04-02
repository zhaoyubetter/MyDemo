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
class MyCanvas7Layer(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.FILL
        }

        canvas.apply {
            drawColor(Color.WHITE)
            drawCircle(100f,100f, 85f, paint)

            paint.color = Color.BLUE
            // 创建一个新的layer，后续的蓝色圆是在这个 layer 中绘制，与 红色圆 不是同一个layer
            saveLayerAlpha(0f, 0f, 300f,300f,0x88)  // 最后参数为透明度
            drawCircle(150f, 150f, 85f, paint)
            restore()    // 把本次的绘制的图像“绘制”到上层Layer上
            drawLine(0f, 0f, 300f, 300f, paint)
        }
    }
}