package com.github.android.sample.anim

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.view.animation.TranslateAnimation
import com.better.base.ToolbarActivity
import com.better.base.e
import com.better.base.model.SampleItem
import com.better.base.toast
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_property_anim1.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class PropertyAnim1Activity : ToolbarActivity() {

    private var valueAnim: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_anim1)

        // 设置 toolbar
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            toolbar.title = it.title
            toolbar.subtitle = it.desc
        }

        // start anim
        btn_tween_start.onClick {
            val anim = TranslateAnimation(0f, 200f, 0f, 400f).apply {
                duration = 2000
                fillAfter = true
            }
            btn_tween.startAnimation(anim)
        }


        // start property anim
        btn_prop_start.onClick {
            val oldLeft = btn_prop.left
            val oldTop = btn_prop.top
            valueAnim = ValueAnimator.ofInt(0, 200, 0).apply {
                duration = 800     // 持续时间

                // 监听器1
                addUpdateListener { it ->
                    e("animValue: ${it.animatedValue} , fraction: ${it.animatedFraction}")
                    val left = oldLeft + (it.animatedValue) as Int
                    val top = oldTop + (it.animatedValue) as Int
                    btn_prop.layout(
                            left, top,
                            left + btn_prop.width,
                            top + btn_prop.height)
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                }

                // 监听器2,监听动画状态
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })

                setEvaluator(IntEvaluator())
            }
            valueAnim?.start()
        }

        // tween click event
        btn_tween.onClick {
            toast("Tween Button is Clicked!")
        }
        btn_prop.onClick {
            toast("Property Button is Clicked!")
        }


        // evaluator
        btn_evaluator.onClick {
            val animator = ValueAnimator.ofInt(-0x100, -0xffff01)
            animator.setEvaluator(ArgbEvaluator())
            animator.duration = 3000
            animator.addUpdateListener { animation ->
                val curValue = animation.animatedValue as Int
                btn_evaluator.setBackgroundColor(curValue)
            }
            animator.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        valueAnim?.cancel()
    }
}
