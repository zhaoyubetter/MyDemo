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
class PathView3(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val path = Path()
    // 控制点： 手指的前一个点，用来当控制点
    var prevX = 0f
    var prevY = 0f

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
                prevX = event.x
                prevY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
//                path.lineTo(event.x, event.y)
                // 结束点 为线段的中间位置
                val endX = (event.x + prevX) / 2
                val endY = (event.y + prevY) / 2
                path.quadTo(prevX, prevY, endX, endY)
                prevX = endX
                prevY = endY
                postInvalidate()
            }
        }
        return super.onTouchEvent(event)
    }
}