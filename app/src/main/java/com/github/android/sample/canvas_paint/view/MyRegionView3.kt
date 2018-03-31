package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/3/31.
 */
class MyRegionView3(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private var op = Region.Op.INTERSECT

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            color = Color.RED
            style = Paint.Style.STROKE
        }

        val rect1 = Rect(200, 200, 400, 260)
        val rect2 = Rect(270, 130, 330, 330)

        canvas.let {
            it.drawRect(rect1, paint)
            it.drawRect(rect2, paint)
        }

        // 集合操作
        val paint2 = Paint().apply {
            style = Paint.Style.FILL
            color = Color.GREEN
        }
        val region1 = Region(rect1)
        val region2 = Region(rect2)
        region1.op(region2, op)     // Region.Op.INTERSECT
        drawRegion(canvas, region1, paint2)

        // 文字说明
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 28f
        canvas.drawText("横为region1", 300f, 450f, paint)
        canvas.drawText("竖为region2", 300f, 480f, paint)
        canvas.drawText("操作为：region1.op(region2, op)", 300f, 510f, paint)
    }

    fun setOp(op: Region.Op) {
        this.op = op
        invalidate()
    }

    private fun drawRegion(canvas: Canvas, rgn: Region, paint: Paint) {
        val iter = RegionIterator(rgn)
        val r = Rect()
        while (iter.next(r)) {
            canvas.drawRect(r, paint)
        }
    }
}