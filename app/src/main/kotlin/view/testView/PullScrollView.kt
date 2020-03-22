package view.testView

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.TranslateAnimation
import android.widget.ScrollView


/**
 * 参考：https://blog.csdn.net/harvic880925/article/details/46728247
 */
class PullScrollView(ctx: Context, attrs: AttributeSet? = null) : ScrollView(ctx, attrs) {

    private lateinit var headerView: View
    private lateinit var contentView: View
    private var downPoint = Point()
    private val headViewRect = Rect(-118, -118, -118, -118)
    private val contentViewRect = Rect(-118, -118, -118, -118)

    private val SCROLL_RATIO = 0.38
    private var isMoving = false
    // 拖动时禁止ScollView本身的滚动行为
    private var enableMoving = false

    // 当用户先将布局向上滚，然后再点击向下拉。这时我们获取到的初始值就会出错。
    // 只允许布局在初始状态时，才允许用户滚动, getScrollY()是不是等于0，当等于0时，为初始化状态
    private var isLayout = false

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentView = getChildAt(0)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // 保存点击位置，如果子View消费了down，那么viewGroup的onTouchEvent收不到任何事件了，
        // 所以这里需要记录原始位置
        if (ev.action == ACTION_DOWN) {
            initRect(ev)
            //如果当前不是从初始化位置开始滚动的话，就不让用户拖拽
            if (scrollY == 0) {
                isLayout = true
            }
        } else if (ev.action == ACTION_MOVE) {
            // 同理，我们不能确保 子View 不消费 ACTION_MOVE，所以这里要特殊处理一下
            //如果当前的事件是我们要处理的事件时，比如现在的下拉，这时候，我们就不能让子控件来处理这个事件
            //这里就需要把它截获，不传给子控件，更不能让子控件消费这个事件
            //不然子控件的行为就可能与我们的相冲突
            var dy = (ev.y - downPoint.y)
            if (dy >= ViewConfiguration.getTouchSlop()) {
                onTouchEvent(ev)
                return true
            }
        } else if (ev.action == ACTION_UP || ev.action == ACTION_CANCEL) {
            resetRect()
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            ACTION_DOWN -> {
                initRect(ev)
            }
            ACTION_MOVE -> {
                onMove(ev)
            }
            ACTION_UP, ACTION_CANCEL -> {
                goBack(ev)
                resetRect()
                enableMoving = false
            }
        }
        return enableMoving || super.onTouchEvent(ev)
    }

    private fun onMove(ev: MotionEvent) {
        var dy = (ev.y - downPoint.y)
        dy = if (dy > headerView!!.height) headerView!!.height.toFloat() else dy

        Log.d("viewBase", "dy:$dy, scrollY:$scrollY")
        // 只能下拉 && 向下滚动
        if (dy > 0 && dy >= scrollY && isLayout) {
            val headerMoveDist = dy * 0.5 * SCROLL_RATIO        // header 距离减少一半
            val headerCurTop = (headViewRect.top + headerMoveDist).toInt()
            val headerCurBottom = (headViewRect.bottom + headerMoveDist).toInt()

            val contentMoveDist = dy * SCROLL_RATIO
            val contentTop = (contentViewRect.top + contentMoveDist).toInt()
            val contentBottom = (contentViewRect.bottom + contentMoveDist).toInt()

            // contentView的上边沿不能低于headView的底边,即：2个view不能分离
            if (contentTop <= headerCurBottom) {
                headerView.layout(headViewRect.left, headerCurTop, headViewRect.right, headerCurBottom)
                contentView.layout(contentViewRect.left, contentTop, contentViewRect.right, contentBottom)
                isMoving = true
                enableMoving = true
            }
        }
    }

    private fun initRect(ev: MotionEvent) {
        if (contentViewRect.top == -118 && headerView != null) {
            headViewRect.set(headerView!!.left, headerView!!.top, headerView!!.right, headerView!!.bottom)
            contentViewRect.set(contentView.left, contentView.top, contentView.right, contentView.bottom)
            downPoint.set(ev.x.toInt(), ev.y.toInt())
        }
    }

    private fun goBack(ev: MotionEvent) {
        if (isMoving) {
            val headerCurTop = headerView.top
            val contentCurTop = contentView.top
            headerView.layout(headViewRect.left, headViewRect.top, headViewRect.right, headViewRect.bottom)
            contentView.layout(contentViewRect.left, contentViewRect.top, contentViewRect.right, contentViewRect.bottom)
            val translationHeaderView = TranslateAnimation(0f, 0f, (headerCurTop - headViewRect.top).toFloat(), 0f)
            val translationContentView = TranslateAnimation(0f, 0f, (contentCurTop - contentViewRect.top).toFloat(), 0f)
            translationHeaderView.duration = 200
            translationContentView.duration = 200
            headerView.startAnimation(translationHeaderView)
            contentView.startAnimation(translationContentView)
            isMoving = false
        }
    }

    private fun resetRect() {
        contentViewRect.top = -118
        isMoving = false
        isLayout = false
    }


    fun setHeadView(view: View) {
        this.headerView = view
    }
}