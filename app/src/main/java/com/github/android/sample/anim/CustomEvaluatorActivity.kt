package com.github.android.sample.anim

import android.animation.TypeEvaluator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_custom_evaluator.*
import org.jetbrains.anko.find



class CustomEvaluatorActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_evaluator)
        // 设置 toolbar
        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            toolbar.title = it.title
            toolbar.subtitle = it.desc
        }
        btn_start.setOnClickListener {
            find<MyPointerView>(R.id.view).doAnim()
        }
    }

    class PointerEvaluator:TypeEvaluator<Pointer> {
        override fun evaluate(fraction: Float, startValue: Pointer, endValue: Pointer): Pointer {
            val start = startValue.radius
            val end = endValue.radius
            val curValue = (start + fraction * (end - start)).toInt()

            val startInt = startValue.color
            val startA = (startInt shr 24 and 0xff) / 255.0f
            var startR = (startInt shr 16 and 0xff) / 255.0f
            var startG = (startInt shr 8 and 0xff) / 255.0f
            var startB = (startInt and 0xff) / 255.0f
            val endInt = endValue.color
            val endA = (endInt shr 24 and 0xff) / 255.0f
            var endR = (endInt shr 16 and 0xff) / 255.0f
            var endG = (endInt shr 8 and 0xff) / 255.0f
            var endB = (endInt and 0xff) / 255.0f
            // convert from sRGB to linear
            startR = Math.pow(startR.toDouble(), 2.2).toFloat()
            startG = Math.pow(startG.toDouble(), 2.2).toFloat()
            startB = Math.pow(startB.toDouble(), 2.2).toFloat()
            endR = Math.pow(endR.toDouble(), 2.2).toFloat()
            endG = Math.pow(endG.toDouble(), 2.2).toFloat()
            endB = Math.pow(endB.toDouble(), 2.2).toFloat()
            // compute the interpolated color in linear space
            var a = startA + fraction * (endA - startA)
            var r = startR + fraction * (endR - startR)
            var g = startG + fraction * (endG - startG)
            var b = startB + fraction * (endB - startB)

            // convert back to sRGB in the [0..255] range
            a = a * 255.0f
            r = Math.pow(r.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
            g = Math.pow(g.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
            b = Math.pow(b.toDouble(), 1.0 / 2.2).toFloat() * 255.0f

            return Pointer(curValue, Math.round(a) shl 24 or (Math.round(r) shl 16) or (Math.round(g) shl 8) or Math.round(b))
        }
    }
    data class Pointer(var radius:Int, var color:Int)

}




