package com.github.widget.better

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * 再次编写FlowLayout
 */
class FlowLayout(ctx: Context, attrs: AttributeSet? = null) : ViewGroup(ctx, attrs) {

    /**
     * 注意 padding 与 margin
     */
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
            var lp: MarginLayoutParams? = null
            if (it.layoutParams is MarginLayoutParams) {
                lp = it.layoutParams as MarginLayoutParams
            }

            // include margin
            val childWidth = (lp?.leftMargin ?: 0) + it.measuredWidth + (lp?.rightMargin ?: 0)
            val childHeight = it.measuredHeight + (lp?.topMargin ?: 0) + (lp?.bottomMargin ?: 0)

            // need wrapLine
            if (lineWidth + childWidth >= widthSize - paddingLeft - paddingRight) {
                resultHeight += lineHeight
                resultWidth = Math.max(lineWidth, childWidth)

                lineWidth = childWidth
                lineHeight = childHeight
            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }
        }

        // last line
        // 因为是在每次换行时，高度增加，所以循环结束的时候，高度需要增加
        resultHeight += lineHeight + paddingTop + paddingBottom
        resultWidth = Math.max(lineWidth, resultWidth) + paddingLeft + paddingRight

        setMeasuredDimension(if (widthMode == MeasureSpec.EXACTLY) widthSize else resultWidth,
                if (heightMode == MeasureSpec.EXACTLY) heightSize else resultHeight)
    }

    /**
     * layout 时，需要考虑 margin 与 padding
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var offsetLeft = 0
        var offsetTop = 0

        var lineWidth = 0
        var lineHeight = 0

        (0 until childCount).map { getChildAt(it) }.filter { it.visibility != View.GONE }.forEach {
            var lp: MarginLayoutParams = it.layoutParams as MarginLayoutParams
            val childWidth = it.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = it.measuredHeight + lp.topMargin + lp.bottomMargin

            // need wrap
            if (lineWidth + childWidth >= width - paddingLeft - paddingRight) {
                offsetLeft = 0
                offsetTop += lineHeight

                lineWidth = childWidth
                lineHeight = childHeight
            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
            }

            // 子 padding 由子自己处理
            val lc = paddingLeft + offsetLeft + lp.leftMargin
            val tc = paddingTop + offsetTop + lp.topMargin
            val rc = lc + it.measuredWidth
            var bc = tc + it.measuredHeight
            it.layout(lc, tc, rc, bc)
            offsetLeft += childWidth
        }
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }
}