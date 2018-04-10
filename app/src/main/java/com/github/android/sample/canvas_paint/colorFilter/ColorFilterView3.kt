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

    private var bitmap: Bitmap
    private var paint: Paint
    val colorMatrix = ColorMatrix()

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.icon_map)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制原始位图
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 64, 64 * bitmap.height / bitmap.width), paint)

        val paint2 = Paint(Paint.ANTI_ALIAS_FLAG)
        canvas.translate(260f, 0f)
        paint2.colorFilter = PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)     // 正片叠底
        canvas.drawBitmap(bitmap, null, Rect(0, 0, 64, 64 * bitmap.height / bitmap.width), paint2)
    }
}