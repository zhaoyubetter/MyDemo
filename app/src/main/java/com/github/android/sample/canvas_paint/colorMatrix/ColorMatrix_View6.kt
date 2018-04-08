package com.github.android.sample.canvas_paint.colorMatrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

/**
 * Created by zhaoyu1 on 2018/4/3.
 */
class ColorMatrix_View6(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private var bitmap: Bitmap
    private var paint: Paint

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.juntuan)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制原始位图
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)

        canvas.translate(260f, 0f)

        // 通道输出（红色）
        var colorMatrix = ColorMatrix(floatArrayOf(
                1f, 0f, 0f, 0f, 0f,     // RED
                0f, 0f, 0f, 0f, 0f,     // GREEN
                0f, 0f, 0f, 0f, 0f,     // BLUE
                0f, 0f, 0f, 1f, 0f))    // ALPHA
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)

        canvas.translate(-260f, 230f)
        // 通道输出(绿)
        colorMatrix = ColorMatrix(floatArrayOf(
                0f, 0f, 0f, 0f, 0f,     // RED
                0f, 1f, 0f, 0f, 0f,     // GREEN
                0f, 0f, 0f, 0f, 0f,     // BLUE
                0f, 0f, 0f, 1f, 0f))    // ALPHA
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)

        canvas.translate(260f, 0f)
        // 通道输出(蓝)
        colorMatrix = ColorMatrix(floatArrayOf(
                0f, 0f, 0f, 0f, 0f,     // RED
                0f, 0f, 0f, 0f, 0f,     // GREEN
                0f, 0f, 1f, 0f, 0f,     // BLUE
                0f, 0f, 0f, 1f, 0f))    // ALPHA
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)


        canvas.translate(-260f, 230f)
        // 黑白
        colorMatrix = ColorMatrix(floatArrayOf(
                0.33f, 0.33f, 0.33f, 0f, 0f,     // RED
                0.33f, 0.33f, 0.33f, 0f, 0f,     // GREEN
                0.33f, 0.33f, 0.072f, 0f, 0f,   // BLUE
                0f, 0f, 0f, 1f, 0f))    // ALPHA
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)

        // 红绿色反转
        canvas.translate(260f, 0f)
        colorMatrix = ColorMatrix(floatArrayOf(
                0f, 1f, 0f, 0f, 0f,     // RED
                1f, 0f, 0f, 0f, 0f,     // GREEN
                0f, 0f, 1f, 0f, 0f,     // BLUE
                0f, 0f, 0f, 1f, 0f))    // ALPHA
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)


        // 相片变旧
        canvas.translate(-260f, 230f)
        colorMatrix = ColorMatrix(floatArrayOf(
                1/2f,1/2f,1/2f,0f,0f,
                1/3f,1/3f,1/3f,0f,0f,
                1/4f,1/4f,1/4f,0f,0f,
                0f,0f,0f,1f,0f ))    // ALPHA
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)
    }
}