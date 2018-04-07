package com.github.android.sample.canvas_paint.colorMatrix

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


/**
 * Created by zhaoyu1 on 2018/4/3.
 */
class ColorMatrix_View1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            setARGB(255, 200, 100, 100)
        }

        // 绘制原始位图
        canvas.drawRect(0f, 0f, width / 2.toFloat(), 600f, paint)
        canvas.translate(width / 2.toFloat(), 0f)

        // 生成色彩矩阵(把红色和蓝色都去掉，仅显示绿色值)
        val colorMatrix = ColorMatrix(floatArrayOf(
                0f, 0f, 0f, 0f, 0f,     // RED
                0f, 1f, 0f, 0f, 0f,     // GREEN
                0f, 0f, 0f, 0f, 0f,     // BLUE
                0f, 0f, 0f, 1f, 0f))    // ALPHA
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawRect(0f, 0f, width / 2.toFloat(), 600f, paint)
    }
}