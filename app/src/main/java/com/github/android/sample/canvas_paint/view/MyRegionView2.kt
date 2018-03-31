package com.github.android.sample.canvas_paint.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by zhaoyu on 2018/3/31.
 */
class MyRegionView2(ctx: Context, attrs: AttributeSet? = null) : View(ctx, attrs) {


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 2f
            style = Paint.Style.FILL//绘图样式，对于设文字和几何图形都有效
            color = Color.RED
        }

        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE

        // 椭圆路径
        val ovalPath = Path()
        val rect = RectF(50f, 50f, 200f, 500f)
        canvas.drawRect(rect, paint)
        ovalPath.addOval(rect, Path.Direction.CCW)
        // setPath时,传入一个比椭圆区域小的矩形区域,让其取【交集】
        val region = Region()
        region.setPath(ovalPath, Region(50, 50, 200, 300))
        paint.color = Color.RED
//        paint.style = Paint.Style.FILL

        drawRegion(canvas, region, paint)
    }

    private fun drawRegion(canvas: Canvas, rgn: Region, paint: Paint) {
        val iter = RegionIterator(rgn)
        val r = Rect()
        while (iter.next(r)) {
            canvas.drawRect(r, paint)
        }
    }
}