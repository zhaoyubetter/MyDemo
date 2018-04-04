package com.github.android.sample.canvas_paint.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu1 on 2018/4/4.
 */
class PathView1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {
    override fun onDraw(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }
        // 画曲线
        val path = Path().apply {
            moveTo(100f, 300f)
            quadTo(200f, 200f, 300f, 300f)
            quadTo(400f, 400f, 500f, 300f)
        }
        canvas.drawPath(path, paint)
    }
}