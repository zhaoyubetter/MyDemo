package view.testView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * @author zhaoyu1  2021/1/22
 **/
public open class MyRelativeLayout2(ctx: Context, attr: AttributeSet) : RelativeLayout(ctx, attr) {

    lateinit var tag: String

    init {
        tag = "ViewGroup1"
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.dispatchTouchEvent(ev);
        Log.d(tag, "dispatchTouchEvent: ${ev?.action}, result:$result")
        return result
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.onInterceptTouchEvent(ev);
        Log.d(tag, "onInterceptTouchEvent: ${ev?.action}, result:$result")
        return result;
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.onTouchEvent(ev);
        Log.d(tag, "onTouchEvent: ${ev?.action}, result:$result")
        return result;
    }
}

public class MyRelativeLayout1(ctx: Context, attr: AttributeSet) : RelativeLayout(ctx, attr) {

    val tag = "ViewGroup2"

    private var mTouchSlop = 0
    private var mIsScrolling = false
    private var downX = 0
    private var downY = 0

    init {
        val vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.dispatchTouchEvent(ev);
        Log.d(tag, "dispatchTouchEvent: ${ev?.action}, result:$result")
        return result
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(tag, "onInterceptTouchEvent: ${ev?.action}, result:$mIsScrolling")

        val action = ev!!.actionMasked
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsScrolling = false
            downY = 0
            downX = 0
            return false
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x.toInt()
                downY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val x = ev.x.toInt()
                val y = ev.y.toInt()
                val yDiff = Math.abs(y - downY)
                if (yDiff > mTouchSlop) {
//                    val parent = parent
//                    parent?.requestDisallowInterceptTouchEvent(true)
                    mIsScrolling = true
                    downY = ev.y.toInt()
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> mIsScrolling = false
        }

        return mIsScrolling
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
//        return (parent as ViewGroup).dispatchTouchEvent(ev)
        if(ev?.action == MotionEvent.ACTION_MOVE && mIsScrolling) {
            mIsScrolling = false;
            ev?.action = MotionEvent.ACTION_CANCEL
        }
        val result = super.onTouchEvent(ev);
        Log.d(tag, "onTouchEvent: ${ev?.action}, result:$result")
        return result;
    }
}

/**
 * 如果 TextView 设置了 clickable 为 true，所有事件都会到这里（即便上层 MOVE ），并且 onTouchEvent 返回了 true；
 * 这是因为默认的 ViewGroup 是不拦截事件的。
 *
 * 如果我们想在 MOVE 事件，拦截事件，不传给 TextView，需要在 RelatvieLayout1 进行事件拦截，其 ontouchevent 也不处理
 * 这样后面的事件，就不会到底这里了。因为 onTouchevent 返回了 false。
 * 这样
 *
 */
public class MyTextView(ctx: Context, attr: AttributeSet) : TextView(ctx, attr) {
    val tag = "ViewTextView"

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.dispatchTouchEvent(ev);
        Log.d(tag, "dispatchTouchEvent: ${ev?.action}, result:$result")
        return result
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.onTouchEvent(ev);
        Log.d(tag, "onTouchEvent: ${ev?.action}, result:$result")
        return result;
    }
}