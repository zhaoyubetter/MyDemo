package com.github.android.sample.anim

import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.view.View
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_object_animator.*
import org.jetbrains.anko.find

class ObjectAnimatorActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_animator)
        // 设置 toolbar
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            title = it.title
            toolbar.subtitle = it.desc
        }


        find<View>(R.id.btn_alpha).setOnClickListener {
            ObjectAnimator.ofFloat(text, "alpha", 1f, 0f, 1f).setDuration(800).start()
        }

        btn_rotation.setOnClickListener {
            ObjectAnimator.ofFloat(text, "rotation", 0f, 180f, 0f).setDuration(800).start()
        }

        btn_rotation_x.setOnClickListener {
            ObjectAnimator.ofFloat(text, "rotationX", 0f, 180f, 0f).setDuration(800).start()
        }

        btn_rotation_y.setOnClickListener {
            ObjectAnimator.ofFloat(text, "rotationY", 0f, 180f, 0f).setDuration(800).start()
        }

        // translate
        btn_translate_x.setOnClickListener {
            ObjectAnimator.ofFloat(text, "translationX", 0f, 200f, -200f, 0f).setDuration(800).start()
        }

        btn_translate_y.setOnClickListener {
            ObjectAnimator.ofFloat(text, "translationY", 0f, 200f, -200f, 0f).setDuration(800).start()
        }

        // scale
        btn_scale_x.setOnClickListener {
            ObjectAnimator.ofFloat(text, "scaleX", 0f, 2f, 1f).setDuration(800).start()
        }

        btn_scale_y.setOnClickListener {
            ObjectAnimator.ofFloat(text, "scaleY", 0f, 2f, 1f).setDuration(800).start()
        }

    }
}
