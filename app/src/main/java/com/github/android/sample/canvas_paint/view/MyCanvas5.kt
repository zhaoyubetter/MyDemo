package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/3/31.
 */
class MyCanvas5(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.STROKE
            color = Color.RED
        }

        // 填充为灰色
        canvas.drawColor(Color.parseColor("#27000000"))
        // 移动屏幕中间
        canvas.translate(width / 4.toFloat(), height / 2.toFloat())
        val rect1 = RectF(0f, 0f, 200f, 100f)
        val rect2 = RectF(100f, 0f, 300f, 100f)

        val path1 = Path().apply {
            addRect(rect1, Path.Direction.CCW)
        }
        val path2 = Path().apply {
            addRect(rect2, Path.Direction.CCW)
        }

        canvas.drawPath(path1, paint1)
        paint1.color = Color.GREEN
        canvas.drawPath(path2, paint1)

        path1.op(path2, Path.Op.INTERSECT) // 交集
        canvas.clipPath(path1)              // 形成新画布
        canvas.drawColor(Color.YELLOW)
    }
}