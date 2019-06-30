package com.github.android.sample.widget.recyler.layoutman

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import com.better.base.e
import com.better.base.e2


/**
 * https://blog.csdn.net/zxt0601/article/details/52956504
 * 上滑正，下滑负 dy
 * 第二版，实现基本的 flowLayout，可以滑动了
 */
internal class FlowLayoutManager2 : RecyclerView.LayoutManager() {

    /**竖直偏移量 每次换行时，要根据这个offset判断*/
    private var mVerticalOffset: Int = 0//竖直偏移量 每次换行时，要根据这个offset判断
    /**屏幕可见的第一个View的Position*/
    private var mFirstVisiPos: Int = 0//屏幕可见的第一个View的Position
    /**屏幕可见的最后一个View的Position*/
    private var mLastVisiPos: Int = 0//屏幕可见的最后一个View的Position

    //key 是View的position，保存View的bounds ，
    private val itemRects = SparseArray<Rect>()

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        if (itemCount == 0) {                      // 界面没有元素
            detachAndScrapAttachedViews(recycler) // //detach轻量回收所有View
            return
        }

        // getChildCount(),大于0说明是DataChanged()操作 或者（初始化的第二次也会childCount>0）
        if (childCount == 0 && state?.isPreLayout != false) {
            return
        }

        // onLayoutChildren方法在RecyclerView 初始化时 会执行两遍
        detachAndScrapAttachedViews(recycler)
        //初始化
        mVerticalOffset = 0
        mFirstVisiPos = 0
        mLastVisiPos = itemCount

        fill(recycler, state, 0)    // 初始化的时候调用
    }

    /**
     * 1 回收所有屏幕不可见的子View
     * 2 layout所有可见的子View
     * 3 考虑正序(dy>0)与逆序填充(dy<0)
     */
    private fun fill(recycler: RecyclerView.Recycler?, state: RecyclerView.State?, dy: Int): Int {
        if (recycler == null) return dy

        var returnDy = dy
        var topOffset = paddingTop//布局时的上偏移
        var leftOffset = paddingLeft//布局时的左偏移
        var lineMaxHeight = 0//每一行最大的高度

        // === 1.回收阶段===========
        if (childCount > 0) {  //滑动时进来的
            // 倒着来
            (childCount - 1 downTo 0).map { getChildAt(it) }.forEach continuing@{ child ->
                if (dy > 0) { // 上滑，那么是顺序填充,需要回收当前屏幕，上越界的View
                    if (getDecoratedBottom(child!!) - dy < topOffset) {
                        removeAndRecycleView(child!!, recycler)      // 回收
                        mFirstVisiPos++
                        // e(""+(getChildAt(0) as? LinearLayout)?.find<TextView>(android.R.id.text1)?.text)
                        // e("上越界回收....dy ，bottom:${getDecoratedBottom(getChildAt(0))}, dy:$dy" )
                        return@continuing   // continue
                    }
                } else if (dy < 0) { //回收当前屏幕，下越界的View
                    if (getDecoratedTop(child!!) - dy > height - paddingBottom) {
                        removeAndRecycleView(child, recycler)       // 回收
                        mLastVisiPos--
                        return@continuing   // continue
                    }
                }
            }
        }

        // === 2.布局子view阶段===========
        if (dy >= 0) {  // 上滑或者不动
            var minPos = mFirstVisiPos
            mLastVisiPos = itemCount - 1

            if (childCount > 0) {  // 偏移量设置为最后一个view的信息
                val lastView = getChildAt(childCount - 1)!!
                minPos = getPosition(lastView) + 1
                topOffset = getDecoratedTop(lastView)
                leftOffset = getDecoratedRight(lastView)
                lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(lastView))
            }

            // 顺序addView，
            // 从找recycler要一个childItemView,不管它是从scrap里取，还是从RecyclerViewPool里取，亦或是onCreateViewHolder里拿
            for (i in minPos..mLastVisiPos) {
                val child = recycler.getViewForPosition(i)
                addView(child)
                measureChildWithMargins(child, 0, 0)
                val childWidth = getDecoratedMeasurementHorizontal(child)
                val childHeight = getDecoratedMeasurementVertical(child)

                // 保存Rect供逆序layout用
                itemRects.put(i, Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + childWidth,
                        topOffset + childHeight + mVerticalOffset))
                // 不换行
                if (leftOffset + childWidth <= getHorizontalSpace()) {
                    layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + childWidth, topOffset + childHeight)
                    leftOffset += childWidth
                    lineMaxHeight = Math.max(lineMaxHeight, childHeight)
                } else {   // 换行
                    leftOffset = paddingLeft
                    topOffset += lineMaxHeight
                    lineMaxHeight = 0

                    // 新起一行的时候要判断一下边界
                    if (topOffset - dy > height - paddingBottom) {
                        // 越界了 就回收
                        removeAndRecycleView(child, recycler)
                        mLastVisiPos = i - 1
                    } else {  // 视图可见，继续布局
                        layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + childWidth, topOffset + childHeight)

                        // 更新布局信息
                        itemRects.put(i, Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + childWidth,
                                topOffset + childHeight + mVerticalOffset))
                        //改变 left  lineHeight
                        leftOffset += childWidth
                        lineMaxHeight = Math.max(lineMaxHeight, childHeight)
                    }
                }
            }


            //添加完后，判断是否已经没有更多的ItemView，并且此时屏幕仍有空白，则需要修正dy
            val lastChild = getChildAt(childCount - 1)!!
            if (getPosition(lastChild) == itemCount - 1) {
                val gap = height - paddingBottom - getDecoratedBottom(lastChild)
                if (gap > 0) {
                    returnDy -= gap
                }
            }
        } else {  // dy < 0 下滑
            /*
             * ##  利用Rect保存子View边界
            正序排列时，保存每个子rView的Rect，逆序时，直接拿出来layout。
             */
            var maxPos = itemCount - 1
            mFirstVisiPos = 0
            if (childCount > 0) {
                maxPos = getPosition(getChildAt(0)!!) - 1
            }

            e2("better", "maxPos: $maxPos, itemCount:$itemCount, firstViewPos: $mFirstVisiPos")

            for (i in (maxPos downTo mFirstVisiPos)) {
                val rect = itemRects[i]
                e2("better", "index: $i, size: ${itemRects.size()}, rect:$rect")
                if (rect.bottom - mVerticalOffset - returnDy < paddingTop) {
                    mFirstVisiPos = i + 1
                    break
                } else {
                    val view = recycler.getViewForPosition(i)
                    addView(view, 0)//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                    measureChildWithMargins(view, 0, 0)
                    layoutDecoratedWithMargins(view, rect.left, rect.top - mVerticalOffset, rect.right, rect.bottom - mVerticalOffset)
                }
            }


            e("count= [" + childCount + "]" + ",[recycler.getScrapList().size():" + recycler.scrapList.size + ", dy:" + dy + ",  mVerticalOffset" + mVerticalOffset + ", ")
        }

        return returnDy
    }

    /* ----- 滑动支持 start --------------------------------------- */
    override fun canScrollVertically() = true

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        // 位移0，没有子View 不动
        if (dy == 0 || childCount == 0) {
            return 0
        }

        // 上滑正，下滑负 dy
        var realOffset = dy                      // 实际滑动的距离， 会在边界处被修复

        // ==== 边界修复
        if (mVerticalOffset + realOffset < 0) {  // 下滑 =》到达上边界   e("mVerticalOffset=$mVerticalOffset,realOffset=$realOffset,上边界")
            realOffset = -mVerticalOffset
        } else if (realOffset > 0) {            // 上滑
            getChildAt(childCount - 1)?.let {
                // 利用最后一个子View比较修正
                if (getPosition(it) == itemCount - 1) {
                    realOffset = (height - paddingBottom - getDecoratedBottom(it)).let {
                        e("====realOffset:$it")
                        when {
                            (it > 0) -> {         // 最后一个item在父容器的底部偏上，出现概率小
                                -it
                            }     //
                            (it == 0) -> {        // 上滑到达底部  ， e("===> = 0, 上滑到达底部")
                                0
                            }
                            else -> {             // <0 正常上画上滑
                                Math.min(realOffset, -it)
                            }
                        }
                    }
//                    e("=====>mVerticalOffset=$mVerticalOffset,realOffset=$realOffset")
                }
            }
        }

        realOffset = fill(recycler, state, realOffset)  // 先填充，再位移
        mVerticalOffset += realOffset         // 累加实际滑动距离
        offsetChildrenVertical(-realOffset)   // 移动所有的childView
        return realOffset                     // realOffset变量保存实际的位移，也是return 回去的值。大部分情况下它=dy
    }

/* ----- 滑动支持 end --------------------------------------- */

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
