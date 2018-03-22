package com.github.android.sample.anim

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_animator_set.*

class AnimatorSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animator_set)


        val animator1 = ObjectAnimator.ofFloat(text1, "translationY", 0f, 300f, 0f).setDuration(1000)
        val animator2 = ObjectAnimator.ofFloat(text2, "rotation", 0f, -180f, +180f, 0f).setDuration(1000)
        // 0xffff00ff, 0xffffff00, 0xffff00ff
        val animator3 = ObjectAnimator.ofInt(text3, "BackgroundColor", -0xff01, -0x100, -0xff01).setDuration(1000)


        btn_seque.setOnClickListener {
            AnimatorSet().apply {
                duration = 2000
                playSequentially(animator1, animator2, animator3)
            }.start()
        }

        btn_together.setOnClickListener {
            AnimatorSet().apply {
                duration = 2000
                playTogether(animator1, animator2, animator3)
            }.start()
        }


        // AnimatorSet.Builder
        btn_builder.setOnClickListener {
            AnimatorSet().apply {
                val builder = play(animator2).with(animator3)
                builder.after(animator1)
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
            }.start()
        }
    }
}
