package com.github.android.sample.canvas_paint.redPointer

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * 红点拖动效果
 * <pre>
1、拉长效果的实现
2、拉的不够长时返回初始状态
3、拉的够长后显示爆炸消除效果
// 曲线涉及到5个点（2个红点取中间为控制点，4个切点为曲线的起点与终点）
https://blog.csdn.net/harvic880925/article/details/51615221
</pre>
 */
class RedPointerView1(ctx: Context, attrs: AttributeSet? = null) : FrameLayout(ctx, attrs) {

    val radius = 30f
    val startPoint: Point
    val endPoint: Point
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    var isTouch = false
    var path = Path()

    init {
        startPoint = Point(100, 100)
        endPoint = Point()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawCircle(startPoint.x.toFloat(), startPoint.y.toFloat(), radius, paint)

        if (isTouch) {
            canvas.drawCircle(endPoint.x.toFloat(), endPoint.y.toFloat(), radius, paint)

            calculatePath()
            // 曲线涉及到5个点（2个红点取中间为控制点，4个切点为曲线的起点与终点）
            canvas.drawPath(path, paint)
        }
    }

    private fun calculatePath() {
        // ==== 曲线涉及到5个点（2个红点取中间为控制点，4个切点为曲线的起点与终点）

        // === 1.获取2个point的角度
        val dx = endPoint.x - startPoint.x
        val dy = endPoint.y - startPoint.y
        val angleA = Math.atan(dy.toDouble() / dx)

        // startPoint 偏移位置
        val offsetX = radius * Math.sin(angleA)
        val offsetY = radius * Math.cos(angleA)

        // === 2.有了角度后，我们算出4个切点的坐标
        val startX1 = startPoint.x + offsetX
        val startY1 = startPoint.y - offsetY
        val startX2 = startPoint.x - offsetX
        val startY2 = startPoint.y + offsetY

        val endX1 = endPoint.x + offsetX
        val endY1 = endPoint.y - offsetY
        val endX2 = endPoint.x - offsetX
        val endY2 = endPoint.y + offsetY

        // === 3.计算控制点
        val anchorX = (endPoint.x + startPoint.x) / 2 * 1.0f
        val anchorY = (endPoint.y + startPoint.y) / 2 * 1.0f

        // === 4.连接path
        path.reset()
        path.moveTo(startX1.toFloat(), startY1.toFloat())
        path.quadTo(anchorX, anchorY, endX1.toFloat(), endY1.toFloat())
        path.lineTo(endX2.toFloat(), endY2.toFloat())
        path.quadTo(anchorX, anchorY, startX2.toFloat(), startY2.toFloat())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouch = true
                return true
            }
            MotionEvent.ACTION_UP -> isTouch = false
        }
        endPoint.x = event.x.toInt()
        endPoint.y = event.y.toInt()
        postInvalidate()
        return super.onTouchEvent(event)
    }
}