package view

import android.os.Bundle
import android.view.ViewGroup
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_base_view_xand_translation_x.*


class BaseViewXAndTranslationXActivity : ToolbarActivity() {

    override fun finish() {
        overridePendingTransition(0, 0)
        super.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_base_view_xand_translation_x)

        btn_revert.setOnClickListener {
            finish()
            startActivity(getIntent())
        }

        btn_x.postDelayed({
            showInfo()
        }, 200)


        // setX,后 translationX = x - left
        btn_x.setOnClickListener {
            tv_base.x = 100f
            showInfo()
        }

        // setTranslationX, x= left + x
        btn_translate_x.setOnClickListener {
            tv_base.translationX = 100f
            showInfo()
        }

        // 使用布局改变，这里会影响 left
        btn_lp.setOnClickListener {
            val lp = tv_base.layoutParams as ViewGroup.MarginLayoutParams
            lp.leftMargin += 100
            tv_base.layoutParams = lp
            showInfo()
        }
    }

    fun showInfo() {
        tv_info.postDelayed({
            tv_info.text = "left/top:${tv_base.left},${tv_base.top}; x,y: ${tv_base.x},${tv_base.y}; translationX/Y: " +
                    "${tv_base.translationX}, ${tv_base.translationY}"
        }, 100)

    }
}
