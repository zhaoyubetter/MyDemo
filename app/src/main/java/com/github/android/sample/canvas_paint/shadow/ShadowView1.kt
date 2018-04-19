package com.github.android.sample.canvas_paint.shadow

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

class ShadowView1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        color = Color.GREEN
        textSize = 20f
    }
    val bmp = BitmapFactory.decodeResource(resources, R.mipmap.juntuan)

    var radius = 10f
        set(value) {
            field = value
            postInvalidate()
        }
    var dx = 10f
        set(value) {
            field = value
            postInvalidate()
        }
    var dy = 10f
        set(value) {
            field = value
            postInvalidate()
        }

    init {
        // 需禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.apply {
            setShadowLayer(radius, dx, dy, Color.GREEN)
        }
        canvas.drawBitmap(bmp, 80f, 80f, paint)

        canvas.translate(200f, 400f)
        canvas.drawText("Thanks 启舰", 0f, 0f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, 600)
    }

}