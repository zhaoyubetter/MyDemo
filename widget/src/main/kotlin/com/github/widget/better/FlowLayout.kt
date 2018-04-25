package com.github.widget.better

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * 再次编写FlowLayout
 */
class FlowLayout(ctx: Context, attrs: AttributeSet? = null) : ViewGroup(ctx, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var resultHeight = 0
        var resultWidth = 0
        var lineWidth = 0
        var lineHeight = 0

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        (0 until childCount).map { getChildAt(it) }.filter { it.visibility != View.GONE }.forEach {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)

        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }
}