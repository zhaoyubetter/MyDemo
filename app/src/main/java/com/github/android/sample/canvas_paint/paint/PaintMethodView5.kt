package com.github.android.sample.canvas_paint.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * Created by zhaoyu on 2018/4/6.
 */
class PaintMethodView5(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            color = Color.GREEN
            style = Paint.Style.STROKE
        }

        val path = getPath()
        // 原始
        canvas.drawPath(path,paint)

        canvas.translate(0f, 200f)
        paint.pathEffect = DiscretePathEffect(2f, 5f)
        canvas.drawPath(path,paint)

        canvas.translate(0f, 200f)
        paint.pathEffect = DiscretePathEffect(6f, 5f)
        canvas.drawPath(path,paint)

        canvas.translate(0f, 200f)
        paint.pathEffect = DiscretePathEffect(6f, 15f)
        canvas.drawPath(path,paint)
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