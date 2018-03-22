package com.github.android.sample.anim

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.view.View
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_animator_set_sample_demo.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.find

/**
 * 属性动画综合例子
 */
class AnimatorSetSampleDemoActivity : ToolbarActivity() {

    var radius: Int? = null
    var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animator_set_sample_demo)
        // 设置 toolbar
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }

        radius = dip(124)

        // 设置监听
        btn_main.setOnClickListener {
            toggle()
        }
    }

    fun toggle() {
        // 1.分别获取各个按钮的位置 5个按钮，4个角度平分90度
        val degree = 90 * 1.0f / 4
        val totalSet = AnimatorSet()
        (0..4).forEach { it ->
            // 获取x , y 坐标
            val radians = Math.toRadians(degree * it.toDouble())
            val delay = it * 50.toLong()
            val duration = 1000L
            val target = find<View>(baseContext.resources.getIdentifier("btn_${it}", "id", packageName))

            val x = -radius!! * Math.sin(radians).toFloat()
            val y = -radius!! * Math.cos(radians).toFloat()

            lateinit var obj1: ObjectAnimator
            lateinit var obj2: ObjectAnimator
            lateinit var obj3: ObjectAnimator
            lateinit var obj4: ObjectAnimator
            lateinit var obj5: ObjectAnimator
            lateinit var obj6: ObjectAnimator

            if (!isOpen) {
                target.visibility = View.VISIBLE
                obj1 = ObjectAnimator.ofFloat(target, "translationX", 0f, x)
                obj2 = ObjectAnimator.ofFloat(target, "translationY", 0f, y)
                obj3 = ObjectAnimator.ofFloat(target, "scaleX", 0.1f, 1.0f)
                obj4 = ObjectAnimator.ofFloat(target, "scaleY", 0.1f, 1.0f)
                obj5 = ObjectAnimator.ofFloat(target, "alpha", 0.5f, 1.0f)
                obj6 = ObjectAnimator.ofFloat(target, "rotation", 0f, 720f)
                obj6.addUpdateListener {
                    if (it.animatedFraction >= 1.0f) isOpen = true
                }
            } else {
                obj1 = ObjectAnimator.ofFloat(target, "translationX", x, 0f)
                obj2 = ObjectAnimator.ofFloat(target, "translationY", y, 0f)
                obj3 = ObjectAnimator.ofFloat(target, "scaleX", 1.0f, 0.1f)
                obj4 = ObjectAnimator.ofFloat(target, "scaleY", 1.0f, 0.1f)
                obj5 = ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.5f)
                obj6 = ObjectAnimator.ofFloat(target, "rotation", 0f, 720f)
                obj6.addUpdateListener {
                    if (it.animatedFraction >= 1.0f) {
                        target.visibility = View.GONE
                        isOpen = false
                    }
                }
            }

            totalSet.playTogether(AnimatorSet().apply {
                setDuration(duration)
                startDelay = delay
                playTogether(obj1, obj2, obj3, obj4, obj5, obj6)
            })
            totalSet.start()
        }
    }
}
