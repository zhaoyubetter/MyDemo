package com.github.android.sample.canvas_paint.layer

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R


class LayerView3(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    private val wid = 100
    private val hei = 100
    private lateinit var bitmap: Bitmap
    private var paint: Paint

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 2f
        bitmap = BitmapFactory.decodeResource(resources, R.mipmap.drive)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f,0f,200f,200f, paint)
        val layerId = canvas.saveLayerAlpha(100f,100f, 300f,300f, 0x88)
        paint.color = Color.RED
        canvas.drawRect(100f,100f,300f,300f, paint)
        canvas.restoreToCount(layerId)
    }
}