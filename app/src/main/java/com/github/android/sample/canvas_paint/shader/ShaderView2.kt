package com.github.android.sample.canvas_paint.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.github.android.sample.R

class ShaderView2(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {


    val bmp = BitmapFactory.decodeResource(resources, R.mipmap.juntuan)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.MIRROR)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 在矩形内使用指定了shader的画笔作画
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val tHeight = MeasureSpec.getSize(heightMeasureSpec)
//        setMeasuredDimension(widthMeasureSpec, tHeight * 2 / 3)
    }

    fun setTileMode(tileMode: Shader.TileMode) {
        paint.shader = BitmapShader(bmp, tileMode, tileMode)
        postInvalidate()
    }
}