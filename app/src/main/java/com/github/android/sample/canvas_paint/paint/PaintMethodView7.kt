package com.github.android.sample.canvas_paint.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/4/6.
 */
class PaintMethodView7(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            color = Color.GREEN
            style = Paint.Style.STROKE
        }

        // 原始
        val path = getPath()
        canvas.drawPath(path, paint)

        // 圆角特效
        canvas.translate(0f, 150f)
        val cornerEffect = CornerPathEffect(100f)
        paint.pathEffect = cornerEffect
        canvas.drawPath(path, paint.apply { color = Color.RED })

        // 虚线特效
        canvas.translate(0f, 150f)
        val dashEffect = DashPathEffect(floatArrayOf(2f, 5f, 10f, 10f), 0f)
        paint.pathEffect = dashEffect
        canvas.drawPath(path, paint.apply { color = Color.RED })

        // 利用ComposePathEffect先应用圆角特效,再应用虚线特效
        canvas.translate(0f, 150f)
        paint.pathEffect = ComposePathEffect(dashEffect, cornerEffect) //位置交换就只有dashEffect效果
        canvas.drawPath(path, paint)

        // 利用SumPathEffect,分别将圆角特效应用于原始路径,然后将生成的两条特效路径合并
        canvas.translate(0f, 150f)
        paint.pathEffect = SumPathEffect(dashEffect, cornerEffect)
        canvas.drawPath(path, paint)
    }


    private fun getPath(): Path {
        return Path().apply {
            moveTo(0f, 0f)
            for (i in 0..40) {
                lineTo(i * 35f, (Math.random() * 150).toFloat())
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 添加动画
        setOnClickListener {
        }
    }
}