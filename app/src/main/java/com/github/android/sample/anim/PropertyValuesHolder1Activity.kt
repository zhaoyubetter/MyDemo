package com.github.android.sample.anim

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TypeEvaluator
import android.app.Activity
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_property_values_holder1.*


class PropertyValuesHolder1Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_values_holder1)
        // 设置 toolbar
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            title = it.title
            toolbar.subtitle = it.desc
        }

        text.setOnClickListener {
            val rotationHolder = PropertyValuesHolder.ofFloat("Rotation",
                    60f, -60f, 40f, -40f, -20f, 20f, 10f, -10f, 0f)
            val colorHolder = PropertyValuesHolder.ofInt("BackgroundColor",
                    -0x1, -0xff01, -0x100, -0x1)
            ObjectAnimator.ofPropertyValuesHolder(it, rotationHolder, rotationHolder, colorHolder).apply {
                duration = 3000
            }.start()
        }


        btn_property_values_1.setOnClickListener {
            val propertyValuesHolder = PropertyValuesHolder.ofObject("text", object : TypeEvaluator<String> {
                override fun evaluate(fraction: Float, startValue: String, endValue: String): String {
                    val char = (startValue[0].toInt() + (endValue[0].toInt() - startValue[0].toInt()) * fraction).toChar()
                    return Character.toString(char)
                }
            }, 'A', 'Z')
            ObjectAnimator.ofPropertyValuesHolder(btn_property_values_1, propertyValuesHolder).apply {
                duration = 3000
            }.start()
        }
    }

}
