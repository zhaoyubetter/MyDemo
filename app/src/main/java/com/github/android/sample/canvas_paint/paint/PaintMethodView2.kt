package com.github.android.sample.canvas_paint.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/4/6.
 */
class PaintMethodView2(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 40f
            color = Color.GREEN
            style = Paint.Style.STROKE
        }

        Path().let {
            it.moveTo(100f, 100f)
            it.lineTo(200f, 100f)
            it.lineTo(100f, 200f)
            it.close()
            paint.strokeJoin = Paint.Join.MITER     // 锐角
            canvas.drawPath(it, paint)

            it.moveTo(100f, 300f)
            it.lineTo(200f, 300f)
            it.lineTo(100f, 400f)
            it.close()
            paint.strokeJoin = Paint.Join.BEVEL     // 结合处为直线
            canvas.drawPath(it, paint)

            it.moveTo(100f, 500f)
            it.lineTo(200f, 500f)
            it.lineTo(100f, 600f)
            it.close()
            paint.strokeJoin = Paint.Join.ROUND     // 结合处为圆弧
            canvas.drawPath(it, paint)
        }
    }
}