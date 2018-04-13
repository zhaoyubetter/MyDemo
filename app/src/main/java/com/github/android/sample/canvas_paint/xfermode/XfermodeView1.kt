package com.github.android.sample.canvas_paint.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * Created by zhaoyu on 2018/4/9.
 */

class XfermodeView1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

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

        val textY = 210f
        val translateY = 230f

        canvas.save()
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC))
        canvas.drawText("SRC", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP))
        canvas.drawText("SRC_ATOP", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawText("SRC_IN", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC_OUT))
        canvas.drawText("SRC_OUT", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SRC_OVER))
        canvas.drawText("SRC_OVER", 20f,textY,paint)

        canvas.restore()
        // ==== 1 end

        canvas.save()
        canvas.translate(0f, translateY)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST))
        canvas.drawText("DST", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST_ATOP))
        canvas.drawText("DST_ATOP", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        canvas.drawText("DST_IN", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST_OUT))
        canvas.drawText("DST_OUT", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DST_OVER))
        canvas.drawText("DST_OVER", 20f,textY,paint)
        canvas.restore()
        ///////=== 2 end

        canvas.save()

        canvas.translate(0f, translateY*2)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.ADD))
        canvas.drawText("ADD", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.MULTIPLY))
        canvas.drawText("MULTIPLY", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.LIGHTEN))
        canvas.drawText("LIGHTEN", 20f,textY,paint)


        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.OVERLAY))
        canvas.drawText("OVERLAY", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.SCREEN))
        canvas.drawText("SCREEN", 20f,textY,paint)

        canvas.restore()
        //////3===end

        canvas.save()
        canvas.translate(0f, translateY*3)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.DARKEN))
        canvas.drawText("DARKEN", 20f,textY,paint)
        canvas.restore()


        canvas.save()
        canvas.translate(0f, translateY * 4f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.CLEAR))
        canvas.drawText("CLEAR", 20f,textY,paint)

        canvas.translate(180f, 0f)
        mydraw(canvas, PorterDuffXfermode(PorterDuff.Mode.XOR))
        canvas.drawText("XOR", 20f,textY,paint)

        canvas.restore()
    }

    private fun mydraw(canvas: Canvas, mode: PorterDuffXfermode) {
        canvas.drawRect(10f, 10f, 180f, 180f, paint)  // 150 + 30 (10+10+10) = 180
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