package view

import android.os.Bundle
import android.util.Log
import android.view.animation.TranslateAnimation
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_base_view_scroll.*
import kotlinx.android.synthetic.main.activity_tool_attributes_test1.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * ScrollTo/ScrollBy
 */
class BaseViewScrollActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_view_scroll)
        btn_scroll.post {
            showInfo()
        }

        btn_scroll.onClick {
            tv_base.scrollBy(-10, 0)
            showInfo()
        }

        btn_scrollBy.onClick {
            tv_base.scrollBy(0, 10)
            showInfo()
        }

        // tween anim, not change x, y
        btn_anim.onClick {
            val anim = TranslateAnimation(tv_base.getX(), 50f, tv_base.y, 0f)
            anim.fillAfter = true
            tv_base.startAnimation(anim)
            tv_base.postDelayed({
                showInfo()
            }, 200)
        }

        // property anim
        btn_prop.onClick {
            tv_base.animate().x(tv_base.x + 50f).setDuration(0).start()
            showInfo()
        }
    }

    fun showInfo() {
        Log.d("viewBase", String.format("scrollX:%s, scrolly: %s, left:%s, x:%s, y:%s, translationX:%s, translationY:%s",
                tv_base.getScrollX(), tv_base.scrollY, tv_base.getLeft(), tv_base.getX(),
                tv_base.y, tv_base.translationY, tv_base.translationX))
    }
}
