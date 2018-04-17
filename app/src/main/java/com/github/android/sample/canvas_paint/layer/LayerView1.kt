package com.github.android.sample.canvas_paint.layer

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View


class LayerView1(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    private val wid = 100
    private val hei = 100
    private lateinit var dstBmp: Bitmap
    private lateinit var srcBmp: Bitmap
    private var paint: Paint

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.textSize = 25f
        dstBmp = makeDst(wid, hei)
        srcBmp = makeSrc(wid, hei)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawRect(20f,20f,200f,200f,paint)
//        val layerID = canvas.saveLayer(0f, 0f, wid * 2.toFloat(), hei * 2.toFloat(), paint)
//        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
//        canvas.drawBitmap(srcBmp, wid / 2.toFloat(), hei / 2.toFloat(), paint)
//        paint.xfermode = null
//        canvas.restoreToCount(layerID)

//        // 使用save看能否实现噢
        val layerID = canvas.save()
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(srcBmp, wid / 2.toFloat(), hei / 2.toFloat(), paint)
        canvas.restoreToCount(layerID)

    }


    fun makeSrc(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = -0x995501
        c.drawRect(0f, 0f, w.toFloat(), h.toFloat(), p)
        return bm
    }

    fun makeDst(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = -0x33bc
        c.drawOval(RectF(0f, 0f, w.toFloat(), h.toFloat()), p)
        return bm
    }
}