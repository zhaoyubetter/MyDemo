package com.github.android.sample.canvas_paint.shader

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.better.base.w
import com.github.android.sample.R
import android.graphics.Shader
import android.graphics.LinearGradient
import android.widget.TextView
import org.jetbrains.anko.matchParent
import android.graphics.RadialGradient
import android.service.quicksettings.Tile
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import java.time.Duration


class ShaderView12(ctx: Context, attributeSet: AttributeSet? = null) : Button(ctx, attributeSet) {


    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var shade: RadialGradient? = null
    val paint = Paint().apply {}
    var currentX: Float = 0f
    var currentY: Float = 0f
    val DEFAULT_RADIUS = 80f
    var radius = 0f
        set(value) {
            field = value
            if (value > 0) {
                shade = RadialGradient(currentX, currentY, value,
                        0x00ffffff, 0xFF58FAAC.toInt(), Shader.TileMode.CLAMP)
                paint.shader = shade
            }
            postInvalidate()
        }
    var anim: Animator? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(currentX, currentY, radius, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentX = event.x
                currentY = event.y
                radius = DEFAULT_RADIUS
                return true
            }
            MotionEvent.ACTION_UP -> {
                startAnim(400)
            }
        }
        return super.onTouchEvent(event)
    }

    private fun startAnim(duration: Long) {
        anim?.let { it.cancel() }
        anim = ObjectAnimator.ofFloat(this, "radius", DEFAULT_RADIUS, width * 1.0f).apply {
            setDuration(duration)
            interpolator = AccelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    radius = 0f     // 清除效果
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationStart(animation: Animator) {
                }
            })
        }
        anim?.start()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, 268)
    }

}