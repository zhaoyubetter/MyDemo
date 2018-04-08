package com.github.android.sample.canvas_paint.colorMatrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

/**
 * Created by zhaoyu1 on 2018/4/3.
 */

class ColorMatrix_View8(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {


    private var bitmap: Bitmap
    private var paint: Paint
    val colorMatrix = ColorMatrix()

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.juntuan)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.GRAY)
        // 绘制原始位图
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)

        canvas.translate(260f, 0f)

        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        paint2.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, 300)
    }
}