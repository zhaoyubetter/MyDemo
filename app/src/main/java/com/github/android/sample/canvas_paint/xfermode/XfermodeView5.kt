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
 * 波浪
 * Created by zhaoyu on 2018/4/13.
 */

class XfermodeView5(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 2f
        color = Color.BLUE
    }

    val srcBmp = BitmapFactory.decodeResource(resources, R.mipmap.havric)
    val dstBmp = Bitmap.createBitmap(srcBmp.width, srcBmp.height, Bitmap.Config.ARGB_8888)
    val waveWidth = srcBmp.width * 2.0f
    val originY = srcBmp.height / 2.0f - 10f
    var dx = 0f
    val path = Path()

    val anim = ValueAnimator.ofFloat(0f, waveWidth).apply {
        interpolator = LinearInterpolator()
        duration = 2000
        repeatMode = RESTART
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            dx = it.animatedValue as Float
            postInvalidate()
        }
    }

    init {
        anim.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        canvas.save()

        createWave()

        // 生成dst 并清空
        val c = Canvas(dstBmp)
        c.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
        c.drawPath(path, paint)

        // === 画原图
        canvas.drawBitmap(srcBmp, 0f, 0f, paint)
        val layerID2 = canvas.saveLayer(0f, 0f, srcBmp.width.toFloat(), srcBmp.height.toFloat(), paint)
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(srcBmp, 0f, 0f, paint)
        paint.xfermode = null
        canvas.restoreToCount(layerID2)

        canvas.restore()
    }

    private fun createWave() {
        path.apply {
            path.reset()
            // path的起始位置向左移一个波长
            path.moveTo(-waveWidth + dx, originY)
            val halfWaveWidth = waveWidth / 2
            var i = -halfWaveWidth
            while (i <= srcBmp.width + halfWaveWidth) {
                rQuadTo(halfWaveWidth / 2, -50f, halfWaveWidth, 0f)
                rQuadTo(halfWaveWidth / 2, 50f, halfWaveWidth, 0f)
                i += halfWaveWidth
            }

            lineTo(srcBmp.width * 1.0f, srcBmp.height * 1.0f)
            lineTo(0f, srcBmp.height.toFloat())
            close()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        anim?.let {
            it.cancel()
        }
    }
}



