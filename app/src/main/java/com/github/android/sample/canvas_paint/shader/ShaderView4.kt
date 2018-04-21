package com.github.android.sample.canvas_paint.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.better.base.w
import com.github.android.sample.R

class ShaderView4(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {


    val bmp = BitmapFactory.decodeResource(resources, R.mipmap.sanjing)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val shader = BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val matrix = Matrix()
        val size = Math.min(bmp.width, bmp.height)
        val scale = size / Math.min(width, height).toFloat()
        matrix.setScale(scale, scale)
        shader.setLocalMatrix(matrix)

        // 画个外圆
        paint.apply {
            strokeWidth = 5f
            color = Color.GREEN
        }

        canvas.drawCircle(size / 2.toFloat(), size / 2.toFloat(),
                size / 2.toFloat() + 5f, paint)

        paint.shader = shader
        canvas.drawCircle(size / 2.toFloat(), size / 2.toFloat(),
                size / 2.toFloat(), paint)
    }

    // 强制控件大小(正方形)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = Math.min(bmp.width, bmp.height)
        setMeasuredDimension(size + 5, size+5)
    }

}