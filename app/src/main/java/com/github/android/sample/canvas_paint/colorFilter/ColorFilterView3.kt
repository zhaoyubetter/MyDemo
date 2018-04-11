package com.github.android.sample.canvas_paint.colorFilter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R


/**
 * Created by zhaoyu on 2018/4/9.
 */

class ColorFilterView3(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private var mBmp: Bitmap
    private var mPaint: Paint
    val colorMatrix = ColorMatrix()

    init {
        mBmp = BitmapFactory.decodeResource(context.resources, R.mipmap.icon_map)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = 64
        val height = width * mBmp.getHeight() / mBmp.getWidth()

        // 原图
        canvas.drawBitmap(mBmp, null, Rect(0, 0, width, height), mPaint)

        canvas.translate(70f, 0f)
        mPaint.setColorFilter(PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC))
        canvas.drawBitmap(mBmp, null, Rect(0, 0, width, height), mPaint)

        canvas.translate(70f, 0f)
        mPaint.setColorFilter(PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP))
        canvas.drawBitmap(mBmp, null, Rect(0, 0, width, height), mPaint)

        canvas.translate(70f, 0f)
        mPaint.setColorFilter(PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(mBmp, null, Rect(0, 0, width, height), mPaint)

        canvas.translate(70f, 0f)
        mPaint.setColorFilter(PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.SRC_OVER))
        canvas.drawBitmap(mBmp, null, Rect(0, 0, width, height), mPaint)


        canvas.translate(70f, 0f)
        mPaint.setColorFilter(PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP))
        canvas.drawBitmap(mBmp, null, Rect(0, 0, width, height), mPaint)

        // colorMatrix 00FF00
        canvas.translate(70f, 0f)
        mPaint.colorFilter = ColorMatrixColorFilter(ColorMatrix(floatArrayOf(
                0f,0f,0f,0f,0f,
                0f,0f,0f,0f,255f,
                0f,0f,0f,0f,0f,
                0f,0f,0f,1f,0f
        )))
        canvas.drawBitmap(mBmp, null, Rect(0, 0, width, height), mPaint)

    }
}