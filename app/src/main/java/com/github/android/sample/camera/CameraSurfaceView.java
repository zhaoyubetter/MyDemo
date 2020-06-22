package com.github.android.sample.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

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
                        // 拿到真正的预览大小，来设置最终的大小，避免拉伸
                        final Camera.Size size = CameraUtilsOld2.getPreviewSize();
                        if (size != null && size.width > 400) {
                            ViewGroup.LayoutParams lp = CameraSurfaceView.this.getLayoutParams();
                            if (CameraParamUtil.isLandscape(CameraUtilsOld2.getPreviewOrientation())) {
                                lp.width = size.height;
                                lp.height = size.width;
                            } else {
                                lp.width = size.width;
                                lp.height = size.height;
                            }
                            CameraSurfaceView.this.setLayoutParams(lp);
                            Log.d("better", String.format("real size, width:%s, height:%s", lp.width, lp.height));
                        }
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
}