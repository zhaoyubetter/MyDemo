package com.github.android.sample.canvas_paint.layer

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R


class LayerView2(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    private val wid = 100
    private val hei = 100
    private lateinit var bitmap: Bitmap
    private var paint: Paint

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GRAY
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 2f
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.drive)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 最顶层的画纸
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        // 创建一个新的透明画纸，后续的操作会在这个图纸上，不会对之前的造成影响
        //val layerID = canvas.saveLayer(0f, 0f, width * 1.0f, height * 1.0f, paint)
        canvas.skew(1f, 0f)
        canvas.drawRect(0f,0f,100f,100f,paint)
       // canvas.restoreToCount(layerID)
    }
}