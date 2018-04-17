package com.github.android.sample.canvas_paint.layer

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.better.base.e
import com.github.android.sample.R
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.graphics.Canvas.ALL_SAVE_FLAG




class LayerView5(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    private val wid = 100
    private val hei = 100
    private lateinit var bitmap: Bitmap
    private var paint: Paint

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val id1 = canvas.save()

        canvas.clipRect(0, 0, 800, 800)
        canvas.drawColor(resources.getColor(R.color.red_27))
        e("count:" + canvas.saveCount + "  id1:" + id1)

        // 注意：canvas.skew(1f,0f),会影响后续操作
        val id2 = canvas.saveLayer(0f, 0f, width * 1.0f, height * 1.0f, paint)
        canvas.clipRect(100, 100, 700, 700)
        canvas.drawColor(resources.getColor(R.color.green_27))
        e("count:" + canvas.saveCount + "  id2:" + id2)

        val id3 = canvas.saveLayerAlpha(0f, 0f, width * 1.0f, height * 1.0f, 0xf0)
        canvas.clipRect(200, 200, 600, 600)
        canvas.drawColor(resources.getColor(R.color.yellow_27))
        e("count:" + canvas.saveCount + "  id3:" + id3)

        val id4 = canvas.save()
        canvas.clipRect(300, 300, 500, 500)
        canvas.drawColor(resources.getColor(R.color.blue_27))
        e("count:" + canvas.saveCount + "  id4:" + id4)
    }
}