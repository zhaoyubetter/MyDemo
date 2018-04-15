package com.github.android.sample.canvas_paint.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R


/**
 * Created by zhaoyu on 2018/4/13.
 */

class XfermodeView2(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private val wid = 200
    private val hei = 200
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

        // dst 原图
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)

        canvas.translate(0f, 400f)

        // 根据使用绘制流程类，这里没有使用相关函数，不需要关闭硬件加速
        val layerID = canvas.saveLayer(0f, 0f, wid * 2.toFloat(), hei * 2.toFloat(), paint)
        // 1. 先画目标图像(圆)
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        // 2.设置xfermode
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)  // OVERLAY
        // 3.画原图（方），最后在源图像上生成结果图并更新到目标图像上
        canvas.drawBitmap(srcBmp, wid / 2.toFloat(), hei / 2.toFloat(), paint)
        paint.xfermode = null
        canvas.restoreToCount(layerID)

    }

    fun makeSrc(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = 0x78FFFFFF
        c.drawRect(0f, 0f, w.toFloat(), h.toFloat(), p)
        return bm
    }

    fun makeDst(w: Int, h: Int): Bitmap {
        val bm = BitmapFactory.decodeResource(context.resources, R.mipmap.juntuan)
        return bm
    }
}