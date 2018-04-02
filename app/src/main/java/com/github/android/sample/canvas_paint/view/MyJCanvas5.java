package com.github.android.sample.canvas_paint.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhaoyu1 on 2018/4/2.
 */

public class MyJCanvas5 extends View {
    public MyJCanvas5(Context context) {
        super(context);
    }

    public MyJCanvas5(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyJCanvas5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setStrokeWidth(2);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setColor(Color.RED);

        // 填充为灰色
        canvas.drawColor(Color.parseColor("#27000000"));
        // 移动屏幕中间
        canvas.translate(getWidth() / 4, getHeight() / 2);
        RectF rect1 = new RectF(0f, 0f, 200f, 100f);
        RectF rect2 = new RectF(100f, 0f, 300f, 100f);

        Path path1 = new Path();
        Path path2 = new Path();
        path1.addRect(rect1, Path.Direction.CCW);
        path2.addRect(rect2, Path.Direction.CCW);

        canvas.drawPath(path1, paint1);
        paint1.setColor(Color.GREEN);
        canvas.drawPath(path2, paint1);

        path1.op(path2, Path.Op.INTERSECT); // 交集
        canvas.clipPath(path1);              // 形成新画布
        canvas.drawColor(Color.YELLOW);
    }
}
