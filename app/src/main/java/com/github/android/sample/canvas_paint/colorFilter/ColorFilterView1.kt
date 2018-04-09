package com.github.android.sample.canvas_paint.colorFilter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

/**
 * Created by zhaoyu on 2018/4/9.
 */

class ColorFilterView1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private var bitmap: Bitmap
    private var paint: Paint
    val colorMatrix = ColorMatrix()

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.blue_btn)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制原始位图
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint)

        canvas.translate(260f, 0f)

        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        paint2.colorFilter = LightingColorFilter(0x00ff00,0x000000)     // 绿色
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)


        // 加强蓝色
        canvas.translate(0f, 300f)
        paint2.colorFilter = LightingColorFilter(0xffffff,0x0000f0)     // 蓝色加强
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 250, 250 * bitmap.height / bitmap.width), paint2)
    }
}