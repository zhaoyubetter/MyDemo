package com.github.android.sample.anim

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_property_anim_from_xml.*


class PropertyAnimFromXmlActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_anim_from_xml)
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }

        // 装载动画
        val animator1 = AnimatorInflater.loadAnimator(baseContext, R.animator.property_value_animator)
            as ValueAnimator        // 强转一下

        // 播放
        btn_start.setOnClickListener {
            animator1.apply {
                addUpdateListener { it ->
                    val offset = it.animatedValue as Int        //
                    text.layout( offset,offset,text.width+offset,text.height + offset)
                }
            }.start()
        }

    }

}
