package view

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_base_view.*

/**
 * 这里与ScrollX与ScrollY没有任何关系
 */
class BaseViewCoordinateActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_view)
        tv_base.post {
            showInfo()
        }

        val screenW = resources.displayMetrics.widthPixels

        // 从中间拖动，并设置不能超过边界
        tv_base.setOnTouchListener { v, e ->
            var newX = v.x - v.width / 2 + e.x
            var newY = v.y - v.height / 2 + e.y

            if (newX + v.width > screenW) {
                newX = screenW * 1.0f - v.width
            } else if (newX < 0) {
                newX = 0f
            }

            if (newY < 0) {
                newY = 0f
            }

            v.x = newX
            v.y = newY

            tv_motion.text = "touchX: ${e.x}, touchY:${e.y}"
            tv_motion_raw.text = "touchRawX: ${e.rawX}, touchRawY:${e.rawY}"
            showInfo()
            true
        }
    }

    private fun showInfo() {
        tv_left.text = "left: ${tv_base.left}, top:${tv_base.top}, right: ${tv_base.right}, bottom: ${tv_base.bottom}"
        tv_x_y.text = "x: ${tv_base.x}, y:${tv_base.y}"
        tv_translation_x_y.text = "translationX: ${tv_base.translationX}, translationY:${tv_base.translationY}"
        tv_scroll.text = "scrollX: ${tv_base.scrollX}, scrollY:${tv_base.scrollY}"
    }
}
