package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu1 on 2018/3/30.
 */
class PaintFontView1(ctx: Context, attr: AttributeSet? = null) : View(ctx, attr) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            // 1.基本设置
            style = Paint.Style.FILL;//绘图样式，对于设文字和几何图形都有效
            color = Color.RED
            textAlign = Paint.Align.CENTER;//设置文字对齐方式，取值：align.CENTER、align.LEFT或align.RIGHT
            textSize = 48f;//设置文字大小

//            // 2.样式设置
//            isFakeBoldText = true      // 粗体 bold
//            isUnderlineText = true     // 下划线
//            textSkewX = 0.25f       // 设置字体水平倾斜度
//            isStrikeThruText = true // 删除线
//
//            // 3.其他
//            textScaleX = 2f     // 水平方向拉伸
        }

        paint.textSize = 38f
        paint.color = Color.RED
        paint.strokeWidth = 2f              //设置画笔宽度,必须要设置，否则设置style无效
        paint.textAlign = Paint.Align.LEFT
        paint.style = Paint.Style.FILL
        canvas.drawText("填充(FILL) ==》Kotlin 重新学Android", 2f, 100f, paint)

        paint.style = Paint.Style.STROKE
        canvas.drawText("描边(STROKE) ==》Kotlin 重新学Android", 2f, 200f, paint)

        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawText("填充&描边(FILL_AND_STROKE) ==》Kotlin 重新学Android", 2f, 300f, paint)
    }
}