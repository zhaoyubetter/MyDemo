package view.testView

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ScrollView

/**
 * 参考：https://blog.csdn.net/harvic880925/article/details/46543859
 * Created by better On 2020-03-22.
 */
class AutoScrollBackView(ctx: Context, attrs: AttributeSet? = null) : ScrollView(ctx, attrs) {


    // 阻尼系数
    val damp = 0.618f
    var lastY: Float = 0f
    var originalRect: Rect = Rect(-17, -1, -1, -1)
    lateinit var firstChild: View

    override fun onFinishInflate() {
        super.onFinishInflate()
        firstChild = this.getChildAt(0)
    }

    /**
     * let view move by finger
     */
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val curY = e.y
        when (e.action) {
            ACTION_DOWN -> {        // save init the view's rect
                if(originalRect.left == -17) {  // 确保只初始化一次
                    originalRect = Rect(firstChild.left, firstChild.top, firstChild.right, firstChild.bottom)
                }
            }
            ACTION_MOVE -> {
                // curY - lastY 为手指移动距离
                var dy = (curY - lastY) * damp
                Log.d("viewBase", "dy: $dy, top:${firstChild.top}")

                // 通过 layout 实现
                var newTop = firstChild.top + dy.toInt()

                // 设置不能往上滑动
                if (newTop < originalRect.top) {
                    newTop = originalRect.top
                }
                firstChild.layout(firstChild.left, newTop, firstChild.right, firstChild.bottom)
            }

            // 弹性还原
            ACTION_UP, ACTION_CANCEL -> {
                goBack()
            }
        }
        lastY = curY
        return super.onTouchEvent(e)
    }

    // TranslateAnimation的计算的原点不是在父控件的原点，而是控件初始化位置的原点
    private fun goBack() {
        val curTop = firstChild.top
        Log.d("viewBase", "originTop:${originalRect.top}, curTop: ${curTop}")
        // 1. layout 先还原
        firstChild.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom)
        // 2. 使用translateAnimation实现动画，回到滑动位置，先播放动画
        val animation = TranslateAnimation(0f, 0f, (curTop - originalRect.top).toFloat(), 0f)
        animation.duration = 200
        firstChild.startAnimation(animation)
    }
}