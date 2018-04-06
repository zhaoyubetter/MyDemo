package com.github.android.sample.canvas_paint.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/4/6.
 */
class PaintMethodView3(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            color = Color.GREEN
            style = Paint.Style.STROKE
        }

        Path().let {
            it.moveTo(100f,600f)
            it.lineTo(400f,100f)
            it.lineTo(700f,900f)
            canvas.drawPath(it, paint)

            paint.pathEffect = CornerPathEffect(100f)       // radius = 100f
            canvas.drawPath(it, paint.apply { color = Color.BLACK })

            paint.pathEffect = CornerPathEffect(200f)
            canvas.drawPath(it, paint.apply { color = Color.RED })
        }
    }
}