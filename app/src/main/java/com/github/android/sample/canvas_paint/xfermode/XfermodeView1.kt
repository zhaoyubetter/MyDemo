package com.github.android.sample.canvas_paint.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode


/**
 * Created by zhaoyu on 2018/4/9.
 */

class XfermodeView1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private val wid = 150
    private val hei = 150
    private lateinit var dstBmp: Bitmap
    private lateinit var srcBmp: Bitmap
    private var paint: Paint

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        dstBmp = makeDst(wid, hei)
        srcBmp = makeSrc(wid, hei)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC))

        canvas.save()
        canvas.translate(260f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP))

        canvas.translate(260f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC_IN))

        canvas.translate(260f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC_OUT))

        canvas.restore()
        // ==== 1 end

        canvas.save()
        canvas.translate(0f, 260f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC_OVER))

        canvas.translate(260f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST))

        canvas.translate(260f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST_ATOP))

        canvas.translate(260f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        canvas.restore()
        ///////=== 2 end

        canvas.save()

        canvas.translate(0f, 260 * 2f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST_OUT))

        canvas.translate(260f, 260 * 2f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST_OVER))

    }

    private fun mydraw(canvas: Canvas, mode: PorterDuffXfermode) {
        canvas.drawRect(10f, 10f, 255f, 255f, paint)  // 150 + 75 = 235
        canvas.save()
        canvas.translate(10f, 10f)
        canvas.save()
        canvas.translate(10f, 10f)

        // 根据使用绘制流程类，这里没有使用相关函数，不需要关闭硬件加速
        val layerID = canvas.saveLayer(0f, 0f, wid * 2.toFloat(), hei * 2.toFloat(), paint)
        // 1. 先画目标图像(圆)
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        // 2.设置xfermode
        paint.xfermode = mode
        // 3.画原图（方），最后在源图像上生成结果图并更新到目标图像上
        canvas.drawBitmap(srcBmp, wid / 2.toFloat(), hei / 2.toFloat(), paint)
        paint.xfermode = null
        canvas.restoreToCount(layerID)

        canvas.restore()
        canvas.restore()
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