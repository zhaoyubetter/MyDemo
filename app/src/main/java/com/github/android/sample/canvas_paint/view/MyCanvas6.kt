package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/3/31.
 */
class MyCanvas6(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {

            // ===== 多次save

            drawColor(Color.RED)
            save()  // 保存的画布大小为全屏幕大小

            clipRect(Rect(100, 100, 800, 800))
            drawColor(Color.GREEN)
            save() // 保存画布大小为Rect(100, 100, 800, 800)

            clipRect(Rect(200, 200, 700, 700))
            drawColor(Color.BLUE)
            save() // 保存画布大小为Rect(200, 200, 700, 700)

            clipRect(Rect(300, 300, 600, 600))
            drawColor(Color.BLACK)
            save() // 保存画布大小为Rect(300, 300, 600, 600)

            // 在上面clip，并作画
            clipRect(Rect(400, 400, 500, 500))
            drawColor(Color.WHITE)


            // ===== 多次restore
            // 将栈顶的画布状态取出来，作为当前画布，并画成黄色背景 （黑色变黄色）
            restore()
            restore()
            restore()
            drawColor(Color.YELLOW)
        }
    }
}