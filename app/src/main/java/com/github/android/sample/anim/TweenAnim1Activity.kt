package com.github.android.sample.anim

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import org.jetbrains.anko.find

class TweenAnim1Activity : ToolbarActivity() {

    private lateinit var animView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tween_anim1)

        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            toolbar.title = it.title
            toolbar.subtitle = it.desc
        }

        animView = find(R.id.animView)
        // scale
        find<Button>(R.id.btn_scale).setOnClickListener {
            animView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.tween_scale))
        }

        // alpha
        find<View>(R.id.btn_alpha).setOnClickListener {
            animView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.tween_alpha))
        }

        // rotate
        find<View>(R.id.btn_rotate).setOnClickListener {
            animView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.tween_rotate))
        }

        // translate
        find<View>(R.id.btn_translate).setOnClickListener {
            animView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.tween_translate))
        }

        // set
        find<View>(R.id.btn_set).setOnClickListener {
            animView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.tween_set))
        }

        // login_demo
        find<View>(R.id.btn_login_demo).setOnClickListener {
            find<View>(R.id.circle_view).apply {
                it.visibility = View.VISIBLE
                startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.tween_login_scale))
            }
        }
    }
}
