package com.github.android.sample.amin

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
        find<Button>(R.id.btn_scale).setOnClickListener {
            animView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.tween_scale))
        }
    }
}
