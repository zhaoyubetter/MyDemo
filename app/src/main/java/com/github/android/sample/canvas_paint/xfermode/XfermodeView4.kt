package com.github.android.sample.canvas_paint.xfermode

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R
import android.graphics.Bitmap
import android.view.MotionEvent


/**
 * 橡皮檫
 * Created by zhaoyu on 2018/4/13.
 */

class XfermodeView4(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
        color = Color.BLACK
    }

    val srcBmp = BitmapFactory.decodeResource(resources, R.mipmap.sanjing)
    var dstBmp = Bitmap.createBitmap(srcBmp.width, srcBmp.height, Bitmap.Config.ARGB_8888)
    val path = Path()
    var preX = 0f
    var preY = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // create src, draw path to srcBmp, 手势画到 dst 上
        val c = Canvas(dstBmp)
        c.drawPath(path, paint)


        val layerID2 = canvas.saveLayer(0f, 0f, srcBmp.width.toFloat(), srcBmp.height.toFloat(), paint)
        canvas.drawBitmap(dstBmp, 0f, 0f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        canvas.drawBitmap(srcBmp, 0f, 0f, paint)
        paint.xfermode = null
        canvas.restoreToCount(layerID2)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                preX = event.x
                preY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {  // 平滑过渡
                val endX = (preX + event.x) / 2
                val endY = (preY + event.y) / 2
                path.quadTo(preX, preY, endX, endY)
                preX = event.x
                preY = event.y
            }
        }
        postInvalidate()
        return super.onTouchEvent(event)
    }
}



