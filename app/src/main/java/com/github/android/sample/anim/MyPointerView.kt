package com.github.android.sample.anim

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator

class MyPointerView(context: Context, attrs: AttributeSet) :
        View(context,  attrs) {
    var pointer: CustomEvaluatorActivity.Pointer? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        pointer?.let {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = pointer?.color ?: Color.BLACK
            paint.style = Paint.Style.FILL
            canvas.drawCircle(500f, 500f, pointer?.radius?.toFloat() ?: 0f,paint)
        }
    }

    fun doAnim() {
        ValueAnimator.ofObject(CustomEvaluatorActivity.PointerEvaluator(), CustomEvaluatorActivity.Pointer(10, Color.BLACK)
                , CustomEvaluatorActivity.Pointer(300, Color.RED)).apply {
            addUpdateListener { it ->
                pointer = it.animatedValue as CustomEvaluatorActivity.Pointer
                invalidate()
            }
            duration = 3000
            interpolator = BounceInterpolator() as TimeInterpolator
        }.start()
    }
}