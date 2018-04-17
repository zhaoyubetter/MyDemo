package com.github.android.sample.canvas_paint.layer

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import com.better.base.e
import com.github.android.sample.R


class LayerView4(ctx: Context, attributeSet: AttributeSet? = null) : View(ctx, attributeSet) {

    private val wid = 100
    private val hei = 100
    private lateinit var bitmap: Bitmap
    private var paint: Paint

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f,0f,400f,400f, paint)


        val layerId = canvas.saveLayer(300f,300f, 800f,800f, paint)

        canvas.apply {
            drawRect(100f,100f,800f,800f, paint)
            drawColor(Color.RED)
            val id = save()
            e("better===>saveCount ${canvas.saveCount} , ${id}")

            clipRect(400f,400f, 700f,700f)
            drawColor(Color.GRAY)
            val id2 = save()
            e("better===>saveCount ${canvas.saveCount} , ${id2}")

            clipRect(450f,450f, 500f,500f)
            drawColor(Color.YELLOW)
            e("better===>saveCount ${canvas.saveCount}")

            drawLine(450f,450f,600f,600f, paint)    // canvas受限，不能超出
            restore() // 回到400到700f
            drawLine(420f,500f,600f,680f, paint)
        }



        canvas.restoreToCount(layerId)
    }
}