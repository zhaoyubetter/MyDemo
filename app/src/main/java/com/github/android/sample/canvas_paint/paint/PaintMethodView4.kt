package com.github.android.sample.canvas_paint.paint

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.better.base.e


/**
 * Created by zhaoyu on 2018/4/6.
 */
class PaintMethodView4(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    var path1Phase = 0f
    var path2Phase = 15f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            color = Color.GREEN
            style = Paint.Style.STROKE
        }

        Path().let {
            it.moveTo(100f, 600f)
            it.lineTo(400f, 100f)
            it.lineTo(700f, 900f)
            canvas.drawPath(it, paint)

            // 第一条实线长度为20，第二个空线长度为10，第三个实线长为100，第四条空线长充为100
            paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f, 100f, 100f), path1Phase)
            canvas.translate(0f, 100f)
            canvas.drawPath(it, paint.apply { color = Color.BLACK })

            // 设置偏移值
            paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f, 50f, 100f), path2Phase)
            canvas.translate(0f, 100f)
            canvas.drawPath(it, paint.apply { color = Color.RED })
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 添加动画
        setOnClickListener {
            // 20f, 10f, 100f, 100f 和为 230
            val valueAnim1 = ValueAnimator.ofFloat(0f, 230f).apply {
                duration = 1000
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener {
                    path1Phase = it.animatedValue as Float
                    postInvalidate()
                }
            }

            // 区间为(15, -165),请看坐标系就明白了 15 + 165 = 180
            val valueAnim2 = ValueAnimator.ofFloat(15f, -165f).apply {
                duration = 2000
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener {
                    e("size: ${path2Phase}")
                    path2Phase = it.animatedValue as Float
                    postInvalidate()
                }
            }

            // 联合动画
            AnimatorSet().apply {
                play(valueAnim1).with(valueAnim2)
            }.start()
        }
    }
}