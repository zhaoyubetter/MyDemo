package com.github.android.sample.canvas_paint.redPointer

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.github.android.sample.R
import org.jetbrains.anko.firstChild
import org.jetbrains.anko.padding
import org.jetbrains.anko.textColor

/**
 * 红点拖动效果
 * <pre>
1、拉长效果的实现
2、拉的不够长时返回初始状态
3、拉的够长后显示爆炸消除效果
// 曲线涉及到5个点（2个红点取中间为控制点，4个切点为曲线的起点与终点）
https://blog.csdn.net/harvic880925/article/details/51615221

添加松手动画回去效果，有点瑕疵问题
</pre>
 */
class RedPointerView4(ctx: Context, attrs: AttributeSet? = null) : FrameLayout(ctx, attrs) {

    val default_radius = 30f

    // 用来减小startPoint的大小
    var radius = default_radius
    val startPoint: Point
    val endPoint: Point
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }
    var tipText: TextView

    var isTouch = false
    var path = Path()


    init {
        startPoint = Point(100, 100)
        endPoint = Point()
        tipText = TextView(context).apply {
            layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            textSize = 12f
            padding = 20
            text = "99"
            gravity = Gravity.CENTER
            textColor = resources.getColor(android.R.color.white)
            setBackgroundResource(R.drawable.drawable_red_circle)
        }
        addView(tipText)
    }

    // 添加图层，避免在原图层上画
    override fun dispatchDraw(canvas: Canvas) {
        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint)
        if (isTouch) {
            calculatePath()
            canvas.drawCircle(startPoint.x.toFloat(), startPoint.y.toFloat(), radius, paint)
            canvas.drawCircle(endPoint.x.toFloat(), endPoint.y.toFloat(), radius, paint)
            canvas.drawPath(path, paint)
            tipText.x = endPoint.x.toFloat() - tipText.width / 2
            tipText.y = endPoint.y.toFloat() - tipText.height / 2
        } else {
            val anim3 = ValueAnimator.ofInt(endPoint.x, startPoint.x, startPoint.x + 20, startPoint.x).apply {
                duration = 200
                addUpdateListener { it ->
                    endPoint.x = it.animatedValue as Int
                    isTouch = true
                    postInvalidate()
                }
            }
            val anim4 = ValueAnimator.ofInt(endPoint.y, startPoint.y, startPoint.y + 20, startPoint.y).apply {
                duration = 200
                addUpdateListener { it ->
                    endPoint.y = it.animatedValue as Int
                }
            }


            // 添加过度动画
            AnimatorSet().apply {
                play(anim3).with(anim4)
            }.start()

//            tipText.x = startPoint.x.toFloat() - tipText.width / 2
//            tipText.y = startPoint.y.toFloat() - tipText.height / 2
        }
        canvas.restoreToCount(layerId)
        // 先绘制自己，再绘制该控件的所有子控件，反过来，可以看看效果（textview被遮挡了一部分）
        super.dispatchDraw(canvas)
    }

    /**
     * path计算的方法
     */
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

        // === 5.动态计算 (勾股定理) 求出两个圆心之间当前距
        val distance = Math.sqrt(Math.pow((endPoint.x - startPoint.x).toDouble(), 2.toDouble()) + Math.pow((endPoint.y - startPoint.x).toDouble(), 2.toDouble()))
        radius = default_radius - (distance.toFloat() / 15)
        if (radius < 9f) {
            radius = 9f
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val location = IntArray(2)
                tipText.getLocationOnScreen(location)
                val rect = Rect().apply {
                    left = location[0]
                    top = location[1]
                    right = left + tipText.width
                    bottom = top + tipText.height
                }
                if (rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    isTouch = true
                    return true
                }
            }
            MotionEvent.ACTION_UP -> isTouch = false
        }

        // down的时候，不更新view
        if (event.action != MotionEvent.ACTION_DOWN) {
            endPoint.x = event.x.toInt()
            endPoint.y = event.y.toInt()
            postInvalidate()
        }

        return super.onTouchEvent(event)
    }
}