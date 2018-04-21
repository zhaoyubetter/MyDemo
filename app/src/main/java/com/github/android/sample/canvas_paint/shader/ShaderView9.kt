package com.github.android.sample.canvas_paint.shader

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


class ShaderView9(ctx: Context, attributeSet: AttributeSet? = null) : TextView(ctx, attributeSet) {


    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var shade: LinearGradient? = null
    private var mDx = 0f
    private var anim: ValueAnimator? = null

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (shade == null) {
            shade = LinearGradient((-measuredWidth).toFloat(), 0f, 0f, 0f,
                    intArrayOf(currentTextColor, -0xff0100, currentTextColor),
                    floatArrayOf(0f, 0.5f, 1f),
                    Shader.TileMode.CLAMP
            )

            anim = ValueAnimator.ofFloat(0f, 2 * measuredWidth * 1.0f).apply {
                duration = 1500
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener { it ->
                    mDx = it.animatedValue as Float
                    postInvalidate()
                }
            }
            anim?.start()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val matrix = Matrix()
        matrix.setTranslate(mDx, 0f)
        shade?.setLocalMatrix(matrix)
        paint.shader = shade
        super.onDraw(canvas)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        anim?.let {
            it.cancel()
        }
    }
}