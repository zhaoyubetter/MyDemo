package com.github.widget.better

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.github.widget.wrapper.minIndex

/**
 * https://blog.csdn.net/harvic880925/article/details/69787359
 * 瀑布流
 */
class WaterFallLayout(ctx: Context, attrs: AttributeSet? = null) : ViewGroup(ctx, attrs) {


    var hSpace = 2
        set(value) {
            if (value >= 0) {
                field = value
                postInvalidate()
            }
        }
    var vSpace = 2
        set(value) {
            if (value >= 0) {
                field = value
                postInvalidate()
            }
        }

    /**
     * 每列的高度累加
     */
     var top: IntArray

    var columns = 3
        set(value) {
            if (value >= 2) {
                field = value
                top = IntArray(columns)
                postInvalidate()
            }
        }

    /**
     * item 宽
     */
    var childWidth: Int = 0

    var itemClickListener: ((view: View, index: Int) -> Unit)? = null
        set(value) {
            field = value
            (0 until childCount).map { getChildAt(it) }.forEach {
                it.setOnClickListener {
                    value?.invoke(it, indexOfChild(it))
                }
            }
        }

    init {
        top = IntArray(columns)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        // 计算宽
        childWidth = (widthSize - paddingLeft - paddingRight - (columns - 1) * hSpace) / columns
        val expectWidth = if (childCount < columns) {
            childWidth * columns + (columns - 1) * hSpace
        } else {
            widthSize
        }

        // 计算高
        top.forEachIndexed { index, _ -> top[index] = 0 }
        (0 until childCount).map { getChildAt(it) }.filter { it.visibility != View.GONE }.forEach {
            val layoutParams = it.layoutParams as WaterfallLayoutParams
            val childHeight = it.measuredHeight * (childWidth / if (it.measuredWidth == 0) childWidth else it.measuredWidth)// 比例缩放
            val column = top.minIndex() ?: 0

            // 设置布局信息
            layoutParams.left = paddingLeft + column * (childWidth + hSpace)
            layoutParams.top = paddingTop + top[column] + layoutParams.topMargin
            layoutParams.right = layoutParams.left + childWidth
            layoutParams.bottom = layoutParams.top + childHeight

            top[column] += childHeight + vSpace    // 最小值下标，并设置当前总高度
        }

        setMeasuredDimension(if (widthMode == MeasureSpec.AT_MOST) expectWidth else widthSize, top.max()
                ?: MeasureSpec.getSize(heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        (0 until childCount).map { getChildAt(it) }.filter { it.visibility != View.GONE }.forEach {
            val lp = it.layoutParams as WaterfallLayoutParams
            it.layout(lp.left, lp.top, lp.right, lp.bottom)
        }
    }


    override fun addView(child: View, index: Int, params: LayoutParams?) {
        super.addView(child, index, params)
        child.setOnClickListener {
            itemClickListener?.invoke(child, indexOfChild(child))
        }
    }

    /**
     * 这样才会走generateLayoutParams
     */
    override fun checkLayoutParams(p: LayoutParams) = p is WaterfallLayoutParams

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return WaterfallLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return WaterfallLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return WaterfallLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * LayoutParams
     */
    class WaterfallLayoutParams : MarginLayoutParams {

        var left: Int = 0
        var top: Int = 0
        var right: Int = 0
        var bottom: Int = 0

        constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs) {
        }

        constructor(width: Int, height: Int) : super(width, height) {
        }

        constructor(lp: android.view.ViewGroup.LayoutParams) : super(lp) {
        }
    }
}