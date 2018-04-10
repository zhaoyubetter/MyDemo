package com.github.android.sample.canvas_paint.colorFilter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

/**
 * Created by zhaoyu on 2018/4/9.
 */

class ColorFilterView2(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private var bitmap: Bitmap
    private var paint: Paint
    val colorMatrix = ColorMatrix()

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.juntuan)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制原始位图
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)

        paint.color = Color.WHITE
        paint.textSize = 30f
        paint.style = Paint.Style.FILL

        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.translate(260f, 0f)
        paint2.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)     // 正片叠底
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)
        canvas.drawText("MULTIPLY", 0f, 30f, paint)


        canvas.translate(-260f, 230f)
        paint2.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.ADD)     // 饱和相加
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)
        canvas.drawText("ADD", 0f, 30f, paint)

        canvas.translate(260f, 0f)
        paint2.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN)     // 饱和相加
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)
        canvas.drawText("LIGHTEN", 0f, 30f, paint)

        canvas.translate(-260f, 230f)
        paint2.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.OVERLAY)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)
        canvas.drawText("OVERLAY", 0f, 30f, paint)

        canvas.translate(260f, 0f)
        paint2.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SCREEN)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)
        canvas.drawText("SCREEN", 0f, 30f, paint)

        canvas.translate(-260f, 230f)
        paint2.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN)   // 变暗
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)
        canvas.drawText("DARKEN", 0f, 30f, paint)
    }
}