package com.github.android.sample.canvas_paint.path

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu1 on 2018/4/4.
 */
class PathView4(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val path = Path()

    override fun onDraw(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }


        /*
        path.moveTo(100,300);
        path.quadTo(200,200,300,300);
        path.quadTo(400,400,500,300);
         */

        path.moveTo(100f,300f)
        path.rQuadTo(100f, -100f, 200f,0f)
        path.rQuadTo(100f, 100f, 200f, 0f)

        canvas.drawPath(path, paint)
    }

}