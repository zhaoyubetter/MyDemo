package com.github.android.sample.canvas_paint.shadow

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

class ShadowView2(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint = Paint().apply {
        color = Color.RED
        maskFilter = BlurMaskFilter(100f, BlurMaskFilter.Blur.INNER)
    }
    val bmp = BitmapFactory.decodeResource(resources, R.mipmap.juntuan)

    init {
        // 需禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(200f, 200f, 100f, paint)

        canvas.translate(350f, 0f)
        paint.maskFilter = BlurMaskFilter(100f, BlurMaskFilter.Blur.SOLID)
        canvas.drawCircle(200f, 200f, 100f, paint)

        canvas.translate(-350f, 350f)
        paint.maskFilter = BlurMaskFilter(100f, BlurMaskFilter.Blur.NORMAL)
        canvas.drawCircle(200f, 200f, 100f, paint)

        canvas.translate(350f, 0f)
        paint.maskFilter = BlurMaskFilter(100f, BlurMaskFilter.Blur.OUTER)
        canvas.drawCircle(200f, 200f, 100f, paint)

        canvas.translate(-350f, 350f)
        paint.maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.SOLID)
        canvas.drawBitmap(bmp, 200f,200f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        setMeasuredDimension(widthMeasureSpec, 600)
    }

}