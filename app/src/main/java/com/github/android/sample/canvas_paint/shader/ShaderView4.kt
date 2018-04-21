package com.github.android.sample.canvas_paint.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.github.android.sample.R

class ShaderView4(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {


    val bmp = BitmapFactory.decodeResource(resources, R.mipmap.sanjing)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val shader = BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val matrix = Matrix()
        val scale =  bmp.width.toFloat() / width
        matrix.setScale(scale, scale)
        shader.setLocalMatrix(matrix)

        paint.shader = shader
        canvas.drawCircle(width / 2.toFloat(), bmp.width / 2.toFloat(),
                width / 2.toFloat(), paint)
    }

}