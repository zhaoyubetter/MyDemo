package com.github.android.sample.canvas_paint.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.github.android.sample.R

class ShaderView3(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {


    val bmp = BitmapFactory.decodeResource(resources, R.mipmap.animal_)
    var bmpBG: Bitmap? = null

    var dx = -1f
    var dy = -1f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        if (bmpBG == null) {
            // bitmap设置为控件宽高
            bmpBG = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvasBG = Canvas(bmpBG)
            canvasBG.drawBitmap(bmp, null, RectF(0f, 0f, width * 1.0f, height * 1.0f), paint)
        }

        // 画出局部
        if (dx != -1f && dy != -1f) {
            paint.shader = BitmapShader(bmpBG, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            canvas.drawCircle(dx, dy, 150f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                dx = event.x
                dy = event.y
            }
            else -> {
                dx = -1f
                dy = -1f
            }
        }
        postInvalidate()
        return true
    }

}