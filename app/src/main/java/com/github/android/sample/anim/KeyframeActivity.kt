package com.github.android.sample.anim

import android.animation.*
import android.app.Activity
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_keyframe.*

class KeyframeActivity : ToolbarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyframe)

        // 设置 toolbar
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            title = it.title
            supportActionBar?.subtitle = it.desc
        }

        image_view.setOnClickListener {
            val frame0 = Keyframe.ofFloat(0f, 0f)
            val frame1 = Keyframe.ofFloat(0.1f, -20f)
            val frame2 = Keyframe.ofFloat(0.2f, 20f)
            frame2.interpolator = LinearInterpolator() as TimeInterpolator
            val frame7 = Keyframe.ofFloat(0.7f, -20f)
            frame7.interpolator = BounceInterpolator() as TimeInterpolator
            val frame9 = Keyframe.ofFloat(0.9f, -20f)
            val frame10 = Keyframe.ofFloat(1f, 0f)
            val propertyValuesHolder = PropertyValuesHolder.ofKeyframe("rotation", frame0, frame1, frame2
                    , frame7, frame9, frame10)
            ObjectAnimator.ofPropertyValuesHolder(it, propertyValuesHolder).apply {
                duration = 1000
            }.start()
        }

        // keyframe.ofObject
        text.setOnClickListener {
            val frame0 = Keyframe.ofObject(0.0f, 'A')
            val frame1 = Keyframe.ofObject(0.3f, 'F')
            val frame2 = Keyframe.ofObject(1.0f, 'Z')
            // 必要的Evaluator
            val propertyValuesHolder = PropertyValuesHolder.ofKeyframe("CharText", frame0, frame1, frame2).apply {
                setEvaluator(object : TypeEvaluator<Char> {
                    override fun evaluate(fraction: Float, startValue: Char, endValue: Char): Char {
                        return (startValue.toInt() + fraction * (endValue.toInt() - startValue.toInt())).toChar()
                    }
                })
            }
            ObjectAnimator.ofPropertyValuesHolder(text, propertyValuesHolder).apply {
                duration = 2000
            }.start()
        }


        // 抖動與放大
        btn_start.setOnClickListener {
            // 抖動
            val frame0 = Keyframe.ofFloat(0f, 0f)
            val frame1 = Keyframe.ofFloat(0.1f, -20f)
            val frame2 = Keyframe.ofFloat(0.3f, 20f)
            val frame7 = Keyframe.ofFloat(0.5f, -20f)
            val frame8 = Keyframe.ofFloat(0.7f, -20f)
            val frame9 = Keyframe.ofFloat(0.9f, -20f)
            val frame10 = Keyframe.ofFloat(1f, 0f)
            val propertyValuesHolder = PropertyValuesHolder.ofKeyframe("rotation", frame0, frame1, frame2
                    , frame7, frame8, frame9, frame10)

            // 縮放X
            val scaleXFrame0 = Keyframe.ofFloat(0f, 0f)
            val scaleXFrame1 = Keyframe.ofFloat(.1f, 1.1f)
            val scaleXFrame2 = Keyframe.ofFloat(.3f, 1.2f)
            val scaleXFrame3 = Keyframe.ofFloat(.5f, 1.2f)
            val scaleXFrame4 = Keyframe.ofFloat(.7f, 1.2f)
            val scaleXFrame5 = Keyframe.ofFloat(.9f, 1.1f)
            val scaleXFrame6 = Keyframe.ofFloat(1f, 1.0f)
            val scaleXProperty = PropertyValuesHolder.ofKeyframe("scaleX", scaleXFrame0, scaleXFrame1, scaleXFrame2
                    , scaleXFrame3, scaleXFrame4, scaleXFrame5, scaleXFrame6)

            // 縮放Y
            val scaleYFrame0 = Keyframe.ofFloat(0f, 0f)
            val scaleYFrame1 = Keyframe.ofFloat(.1f, 1.1f)
            val scaleYFrame2 = Keyframe.ofFloat(.3f, 1.2f)
            val scaleYFrame3 = Keyframe.ofFloat(.5f, 1.2f)
            val scaleYFrame4 = Keyframe.ofFloat(.7f, 1.2f)
            val scaleYFrame5 = Keyframe.ofFloat(.9f, 1.1f)
            val scaleYFrame6 = Keyframe.ofFloat(1f, 1.0f)
            val scaleYProperty = PropertyValuesHolder.ofKeyframe("scaleY", scaleYFrame0, scaleYFrame1, scaleYFrame2
                    , scaleYFrame3, scaleYFrame4, scaleYFrame5, scaleYFrame6)

            // 組合
            ObjectAnimator.ofPropertyValuesHolder(image_view, propertyValuesHolder, scaleXProperty, scaleYProperty).apply {
                duration = 1000
            }.start()
        }


        AnimatorSet().playSequentially()

    }
}
