package com.github.android.sample.widget.recyler.layoutman

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 第一版，实现基本的 flowLayout，可以展示，但不能滑动
 */
internal class FlowLayoutManager1 : RecyclerView.LayoutManager() {

    private var mVerticalOffset: Int = 0//竖直偏移量 每次换行时，要根据这个offset判断
    private var mFirstVisiPos: Int = 0//屏幕可见的第一个View的Position
    private var mLastVisiPos: Int = 0//屏幕可见的最后一个View的Position

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)

        if (itemCount == 0) {                      // 界面没有元素
            detachAndScrapAttachedViews(recycler!!) // //detach轻量回收所有View
            return
        }

        if (childCount == 0 && state?.isPreLayout != false) {
            return
        }

        // onLayoutChildren方法在RecyclerView 初始化时 会执行两遍
        detachAndScrapAttachedViews(recycler!!)
        //初始化
        mVerticalOffset = 0
        mFirstVisiPos = 0
        mLastVisiPos = itemCount

        fill(recycler, state)
    }

    /**
     * 1 回收所有屏幕不可见的子View
     * 2 layout所有可见的子View
     */
    private fun fill(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        val minPos = mFirstVisiPos
        if (recycler == null) return

        var dy = 0
        var topOffset = paddingTop//布局时的上偏移
        var leftOffset = paddingLeft//布局时的左偏移
        var lineMaxHeight = 0//每一行最大的高度

        (minPos until mLastVisiPos - 1).map { recycler.getViewForPosition(it) }.forEachIndexed { index, it ->
            addView(it)
            measureChildWithMargins(it, 0, 0)
            val childWidth = getDecoratedMeasurementHorizontal(it)
            val childHeight = getDecoratedMeasurementVertical(it)
            if (leftOffset + childWidth <= getHorizontalSpace()) {
                layoutDecoratedWithMargins(it, leftOffset, topOffset, leftOffset + childWidth, topOffset + childHeight)
                leftOffset += childWidth
                lineMaxHeight = Math.max(lineMaxHeight, childHeight)
            } else {    // need wrap
                leftOffset = paddingLeft
                topOffset += lineMaxHeight

                // 新起一行的时候要判断一下边界
                if (topOffset - dy > height - paddingBottom) {
                    // 越界了 就回收
                    removeAndRecycleView(it, recycler)
                    mLastVisiPos = index - 1
                } else {  // 视图可见，继续布局
                    layoutDecoratedWithMargins(it, leftOffset, topOffset, leftOffset + childWidth, topOffset + childHeight)
                    //改变 left  lineHeight
                    leftOffset += childWidth
                    lineMaxHeight = Math.max(lineMaxHeight, childHeight)
                }
            }
        }
    }

    /**
     * 获取某个childView在水平方向所占的空间
     *
     * @param view
     * @return
     */
    inline fun getDecoratedMeasurementHorizontal(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return (getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin)
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     *
     * @param view
     * @return
     */
    inline fun getDecoratedMeasurementVertical(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return (getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin)
    }

    inline fun getVerticalSpace() = height - paddingTop - paddingBottom
    inline fun getHorizontalSpace() = width - paddingLeft - paddingRight
}
