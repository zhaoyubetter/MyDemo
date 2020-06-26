package com.github.android.sample.camera;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.math.MathUtils.clamp;

/**
 * 创建一个SurfaceView，并实现SurfaceHolder的回调。由于Camera在SurfaceView中是通过SurfaceHolder
 * 使得Surfaceview能够预览Camera返回的数据，因此我们需要实现SurfaceHolder 的回调
 * <p>
 * 作者：cain_huang
 * 链接：https://www.jianshu.com/p/9e0f3fc5a3b4
 * 来源：简书
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = CameraSurfaceView.class.getSimpleName();

    private SurfaceHolder mSurfaceHolder;
    private boolean firstTouch = true;
    private float firstTouchLength = 0;
    // 缩放梯度
    private int zoomGradient = 0;
    int handlerTime = 0;

    public CameraSurfaceView(Context context) {
        super(context);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        CameraUtilsOld2.openBackCamera(getContext(), holder, CameraUtils.DESIRED_PREVIEW_FPS);
    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
        if (holder != null) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CameraSurfaceView.this != null && holder != null) {
                        // 真正的设置最佳预览大小
//                        // 拿到真正的预览大小，来设置最终的大小，避免拉伸
//                        final Camera.Size size = CameraUtilsOld2.getPreviewSize();
//                        if (size != null && size.width > 400) {
//                            ViewGroup.LayoutParams lp = CameraSurfaceView.this.getLayoutParams();
//                            if (CameraParamUtil.isLandscape(CameraUtilsOld2.getPreviewOrientation())) {
//                                lp.width = size.height;
//                                lp.height = size.width;
//                            } else {
//                                lp.width = size.width;
//                                lp.height = size.height;
//                            }
//                            CameraSurfaceView.this.setLayoutParams(lp);
//                            Log.d("better", String.format("real size, width:%s, height:%s", lp.width, lp.height));
//                        }
                    }
                }
            }, 400);
        }

        CameraUtilsOld2.startPreviewDisplay(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraUtilsOld2.releaseCamera();
    }

    /**
     * 聚焦与缩放
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    setFocusViewWidthAnimation(event.getX(), event.getY());
                }
                if (event.getPointerCount() == 2) {
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    firstTouch = true;
                }
                if (event.getPointerCount() == 2) {
                    //第一个点
                    float point_1_X = event.getX(0);
                    float point_1_Y = event.getY(0);
                    //第二个点
                    float point_2_X = event.getX(1);
                    float point_2_Y = event.getY(1);

                    float result = (float) Math.sqrt(Math.pow(point_1_X - point_2_X, 2) + Math.pow(point_1_Y -
                            point_2_Y, 2));

                    if (firstTouch) {
                        firstTouchLength = result;
                        firstTouch = false;
                    }
                    // 缩放先去掉
                    if ((int) (result - firstTouchLength) / zoomGradient != 0) {
                        firstTouch = true;
//                        machine.zoom(result - firstTouchLength, CameraInterface.TYPE_CAPTURE);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                firstTouch = true;
                break;
        }
        return true;
    }

    //对焦框指示器动画
    private void setFocusViewWidthAnimation(float x, float y) {
        handleFocus(getContext(), x, y, new FocusCallback() {
            @Override
            public void focusSuccess() {
            }
        });
    }

    public void handleFocus(final Context context, final float x, final float y, final FocusCallback callback) {
        final Camera mCamera = CameraUtilsOld2.getCamera();
        if (mCamera == null) {
            return;
        }
        final Camera.Parameters params = mCamera.getParameters();
        Rect focusRect = calculateTapArea(x, y, 1f, context);
        mCamera.cancelAutoFocus();
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 800));
            params.setFocusAreas(focusAreas);
        } else {
            callback.focusSuccess();
            return;
        }
        final String currentFocusMode = params.getFocusMode();
        try {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(params);
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success || handlerTime > 10) {
                        Camera.Parameters params = camera.getParameters();
                        params.setFocusMode(currentFocusMode);
                        camera.setParameters(params);
                        handlerTime = 0;
                        callback.focusSuccess();
                    } else {
                        handlerTime++;
                        handleFocus(context, x, y, callback);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    private static Rect calculateTapArea(float x, float y, float coefficient, Context context) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / context.getResources().getDisplayMetrics().widthPixels * 2000 - 1000);
        int centerY = (int) (y / context.getResources().getDisplayMetrics().heightPixels * 2000 - 1000);
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    /**
     * 聚焦回调接口
     */
    public interface FocusCallback {
        void focusSuccess();
    }

}