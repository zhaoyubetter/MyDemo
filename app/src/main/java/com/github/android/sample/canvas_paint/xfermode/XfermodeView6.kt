package com.github.android.sample.canvas_paint.xfermode

import android.animation.ValueAnimator
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.view.animation.LinearInterpolator


/**
 * 文字展示效果
 * Created by zhaoyu on 2018/4/13.
 */

class XfermodeView6(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 2f
        color = Color.RED
    }

    val dstBmp = BitmapFactory.decodeResource(resources, R.mipmap.left_right_qijian)
    val srcBmp = Bitmap.createBitmap(dstBmp.width, dstBmp.height, Bitmap.Config.ARGB_8888)
    val waveWidth = dstBmp.width * 1.0f
    var dx = 10f

    val anim = ValueAnimator.ofFloat(10f, waveWidth).apply {
        interpolator = LinearInterpolator()
        duration = 3000
        repeatMode = RESTART
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            dx = it.animatedValue as Float
            postInvalidate()
        }
    }

    init {
        anim.startDelay = 1000
        anim.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.GRAY)
        canvas.save()

        // create src 并且清空
        val c = Canvas(srcBmp)
        c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
        c.drawRect( 0f,0f, dx, dstBmp.height* 1.0f, paint)

        val layerID2 = canvas.saveLayer(0f, 0f, srcBmp.width.toFloat(), srcBmp.height.toFloat(), paint)
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(srcBmp, 0f, 0f, paint)
        paint.xfermode = null
        canvas.restoreToCount(layerID2)

        canvas.restore()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        anim?.let {
            it.cancel()
        }
    }
}



