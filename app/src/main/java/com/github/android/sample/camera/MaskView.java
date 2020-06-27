package com.github.android.sample.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * 参考：
 * https://blog.csdn.net/yanzi1225627/java/article/details/34931759
 */
public class MaskView extends ImageView {
    private static final String TAG = "better";
    private Paint mLinePaint;
    private Paint mAreaPaint;
    private Rect mCenterRect = null;
    private Context mContext;


    public MaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mContext = context;
        widthScreen = mContext.getResources().getDisplayMetrics().widthPixels;
        heightScreen = mContext.getResources().getDisplayMetrics().heightPixels;
    }

    private void initPaint() {
        //绘制中间透明区域矩形边界的Paint
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(30);

        //绘制四周阴影区域
        mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAreaPaint.setColor(Color.LTGRAY);
        mAreaPaint.setStyle(Paint.Style.FILL);
        mAreaPaint.setAlpha(180);
    }

    public void setCenterRect(Rect r) {
        Log.i(TAG, "setCenterRect..." + r.toString());
        this.mCenterRect = r;
        postInvalidate();
    }

    public void clearCenterRect(Rect r) {
        this.mCenterRect = null;
    }

    int widthScreen, heightScreen;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw...");
        if (mCenterRect == null)
            return;
        //绘制四周阴影区域
        canvas.drawRect(0, 0, widthScreen, mCenterRect.top, mAreaPaint);
        canvas.drawRect(0, mCenterRect.bottom + 1, widthScreen, heightScreen, mAreaPaint);
        canvas.drawRect(0, mCenterRect.top, mCenterRect.left - 1, mCenterRect.bottom + 1, mAreaPaint);
        canvas.drawRect(mCenterRect.right + 1, mCenterRect.top, widthScreen, mCenterRect.bottom + 1, mAreaPaint);

        //绘制目标透明区域
        canvas.drawRect(mCenterRect, mLinePaint);
    }
}