package com.github.android.sample.canvas_paint.xfermode

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R
import android.graphics.Canvas.ALL_SAVE_FLAG
import com.better.base.d
import android.graphics.Shader
import android.graphics.LinearGradient
import android.graphics.Bitmap
import android.graphics.PixelFormat






/**
 * Created by zhaoyu on 2018/4/13.
 */

class XfermodeView3(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val srcBmp = BitmapFactory.decodeResource(resources, R.mipmap.juntuan)
        val bmpWidth = srcBmp.width
        val bmpHeight = srcBmp.height
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

       // .1目标图为圆角矩形
        val dstBmp = makeDest(bmpWidth, bmpHeight)

        val layerID = canvas.saveLayer(0f, 0f, bmpWidth.toFloat(), bmpHeight.toFloat(), paint)
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        // 2.设置xfermode
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)  // OVERLAY
        canvas.drawBitmap(srcBmp, 0f, 0f, paint)
        paint.xfermode = null
        canvas.restoreToCount(layerID)


        // === 倒影
        canvas.save()
        canvas.translate(bmpWidth * 1.2f, 0f)
        // 1.画出原图
        canvas.drawBitmap(srcBmp, 0f, 0f, paint)

        // 有渐变度的drawable
        val dstDrawable = resources.getDrawable(R.drawable.drawable_liner_gradient_white)
        val dstBm = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888)
        val c = Canvas(dstBm)
        dstDrawable.setBounds(0, 0, bmpWidth, bmpHeight / 2)
        dstDrawable.draw(c)


        val matrix = Matrix()
        matrix.setScale(1f, -1f)
        val revertBmp = Bitmap.createBitmap(srcBmp, 0, 0, srcBmp.width, srcBmp.height, matrix, true)

        // 2.再画出倒影
        canvas.translate(0f, bmpHeight.toFloat())
        val layerID2 = canvas.saveLayer(0f, 0f, bmpWidth.toFloat(), bmpHeight.toFloat(), paint)
        canvas.drawBitmap(dstBm,0f,0f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(revertBmp, 0f,0f,paint)
        paint.xfermode = null
        canvas.restoreToCount(layerID2)

        canvas.restore()

    }

    fun makeDest(w: Int, h: Int): Bitmap {
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bm)
        val p = Paint(Paint.ANTI_ALIAS_FLAG)
        p.color = Color.GRAY
        c.drawRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), 40f, 40f, p)
        return bm
    }
}

