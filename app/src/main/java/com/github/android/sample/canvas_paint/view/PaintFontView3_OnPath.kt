package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu1 on 2018/3/30.
 */
class PaintFontView3_OnPath(ctx: Context, attr: AttributeSet? = null) : View(ctx, attr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 64f
            color = Color.RED
            strokeWidth = 2f
            style = Paint.Style.STROKE
        }

        // 沿路径绘制
        val text = "风萧萧兮易水寒，壮士一去兮不复返"
        val circlePath1 = Path().apply {
            addCircle(width / 4.toFloat(), 220f, 180f, Path.Direction.CW)  // 逆時針
        }
        val circlePath2 = Path().apply {
            addCircle(width / 4 * 3.toFloat(), 220f, 180f, Path.Direction.CW)
        }
        canvas.drawPath(circlePath1, paint)
        canvas.drawPath(circlePath2, paint)

        paint.color = Color.GREEN
        // 设置0,0 ，注意这里是逆时针画的圆
        canvas.drawTextOnPath(text, circlePath1, 0f, 0f, paint)
        // 设置了偏移,水平100，垂直 30f
        canvas.drawTextOnPath(text, circlePath2, 100f, 30f, paint)
    }
}