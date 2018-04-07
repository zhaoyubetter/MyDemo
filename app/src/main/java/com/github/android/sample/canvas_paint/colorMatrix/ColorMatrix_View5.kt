package com.github.android.sample.canvas_paint.colorMatrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

/**
 * Created by zhaoyu1 on 2018/4/3.
 */
class ColorMatrix_View5(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private var bitmap: Bitmap
    private var paint: Paint

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.chrome)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制原始位图
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)

        canvas.translate(0f, 260f)

        // 色彩缩放，（全部为增加亮度）
        val colorMatrix = ColorMatrix(floatArrayOf(
                1.2f, 0f, 0f, 0f, 0f,     // RED
                0f, 1.2f, 0f, 0f, 0f,    // GREEN
                0f, 0f, 1.2f, 0f, 0f,     // BLUE
                0f, 0f, 0f, 1.2f, 0f))    // ALPHA
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)
    }
}