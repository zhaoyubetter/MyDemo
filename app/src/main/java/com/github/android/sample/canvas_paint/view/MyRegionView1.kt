package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


/**
 * Created by zhaoyu on 2018/3/31.
 */
class MyRegionView1(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {

    private val region = Region(10, 10, 100, 100)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.FILL;//绘图样式，对于设文字和几何图形都有效
            color = Color.RED
        }
        drawRegion(canvas, region, paint)
    }

    private fun drawRegion(canvas: Canvas, rgn: Region, paint: Paint) {
        val iter = RegionIterator(rgn)
        val r = Rect()
        while (iter.next(r)) {
            canvas.drawRect(r, paint)
        }
    }

    fun setRegion(region: Region) {
        this.region.set(region)
        invalidate()
    }
}