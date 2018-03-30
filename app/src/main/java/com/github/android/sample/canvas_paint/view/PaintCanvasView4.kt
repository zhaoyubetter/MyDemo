package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * path路径
 * Created by zhaoyu1 on 2018/3/26.
 */
class PaintCanvasView4(ctx: Context, attr: AttributeSet? = null) : View(ctx, attr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE//填充样式改为描边
            strokeWidth = 2f//设置画笔宽度
        }

        // 三角形
        val path = Path().apply {
            moveTo(10f, 10f)
            lineTo(10f, 100f)//第一条直线的终点，也是第二条直线的起点
            lineTo(300f, 100f)//画第二条直线
            close() //形成闭环
        }

        // 矩形
        path.let {
            it.addRect(RectF(10f, 110f, 10f + 300f, 110f + 110f), Path.Direction.CW)
        }

        // 圆角矩形
        path.let {
        }

        canvas.drawPath(path, paint)
    }
}