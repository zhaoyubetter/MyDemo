package com.github.android.sample.canvas_paint.shadow

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

class ShadowView3(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint = Paint().apply {
        color = Color.GREEN
    }
    val bmp = BitmapFactory.decodeResource(resources, R.mipmap.icon_lion)
    val alphaBmp = bmp.extractAlpha()

    init {
        // 需禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bmp, 0f, 0f, paint)

        val bmpWidth = bmp.width
        val bmpHeight = bmp.height

        canvas.translate(0f, bmp.height + 20.toFloat())

// 新建一张空白图片，图片具有与原图片一样的Alpha值，这个新建的Bitmap做为结果返回。这个空白图片中每个像素都具有与原图片一样的Alpha值，
// 而且具体的颜色时，只有在使用canvas.drawBitmap绘制时，由传入的paint的颜色指定。

//        paint.color = Color.GRAY
//        canvas.drawBitmap(alphaBmp, 0f, 0f, paint)
//
//        paint.color = Color.BLUE
//        canvas.drawBitmap(alphaBmp, bmp.width + 50f, 0f, paint)


        // 画阴影
        paint.color = Color.GREEN
        paint.maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
        canvas.drawBitmap(alphaBmp, 10f, 10f, paint)

        // 画原图像
        paint.maskFilter = null
        canvas.drawBitmap(bmp, 0f, 0f, paint)
    }

}