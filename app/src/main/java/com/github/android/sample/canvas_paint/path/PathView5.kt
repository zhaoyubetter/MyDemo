package com.github.android.sample.canvas_paint.path

import android.animation.ValueAnimator
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Created by zhaoyu1 on 2018/4/4.
 */
class PathView5(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val path = Path()
    // 波浪高
    val waveHeight = 100f
    // 波浪宽
    val waveWidth = 800f
    // 波浪起始位置
    var waveY = 300f
    // 波浪偏移
    var waveWidthDx = 0f
    // 不断缩小范围动画
    var waveHeightDx = 0f

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    override fun onDraw(canvas: Canvas) {
        path.apply {
            reset()  // 复位

            // === 1.画波浪 ===
            // path的起始位置向左移一个波长
            moveTo(-waveWidth + waveWidthDx, waveY + waveHeightDx)
            val halfWaveWidth = waveWidth / 2
            var i = -halfWaveWidth
            // 画出屏幕内所有的波浪
            while (i <= width + halfWaveWidth) {
                rQuadTo(halfWaveWidth / 2.0f, -waveHeight, halfWaveWidth, 0f)
                rQuadTo(halfWaveWidth / 2.0f, waveHeight, halfWaveWidth, 0f)
                i += halfWaveWidth
            }

            // === 2.闭合path，实现fill效果  ===
            path.lineTo(width.toFloat(), height.toFloat())
            path.lineTo(0f, height.toFloat())
            path.close()

        }
        canvas.drawPath(path, paint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.setOnClickListener {
            // === 3.第三步动画，实现波浪动画
            val valueAnimator = ValueAnimator.ofFloat(0f, waveWidth).apply {
                duration = 2000
                repeatMode = RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener { it ->
                    waveWidthDx = it.animatedValue as Float
                    postInvalidate()
                }
            }
            valueAnimator.start()


            // === 4. 不断缩小范围动画
            ValueAnimator.ofFloat(0f, height.toFloat()).apply {
                duration = 8000
                repeatMode = RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener { it ->
                    waveHeightDx = it.animatedValue as Float
                    postInvalidate()
                }
            }.start()

            // === 可以使用联合动画
        }
    }
}