package com.github.android.sample.canvas_paint.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by zhaoyu1 on 2018/4/4.
 */
class PathView2(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val path = Path()

    override fun onDraw(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                postInvalidate()
            }
        }
        return super.onTouchEvent(event)
    }
}