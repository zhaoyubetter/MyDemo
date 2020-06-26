package com.github.android.sample.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

public class CameraUtilsOld2 {

    // 相机默认宽高，相机的宽度和高度跟屏幕坐标不一样，手机屏幕的宽度和高度是反过来的。
    public static final int DESIRED_PREVIEW_FPS = 30;

    private static int mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static Camera mCamera;
    private static int mCameraPreviewFps;
    private static int mOrientation = 0;
    private static Camera.Size previewSize;

    public static Camera.PreviewCallback previewCallback;

    /**
     * 打开相机，默认打开前置相机
     *
     * @param expectFps
     */
    public static void openBackCamera(final Context context, final SurfaceHolder holder, int expectFps) {
        if (holder == null) {
            return;
        }

        if (mCamera != null) {
            throw new RuntimeException("camera already initialized!");
        }
        Camera.CameraInfo info = new Camera.CameraInfo();
        int numCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open(i);
                mCameraID = info.facing;
                break;
            }
        }
        // 如果没有前置摄像头，则打开默认的后置摄像头
        if (mCamera == null) {
            mCamera = Camera.open();
            mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        // 没有摄像头时，抛出异常
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }

        // 设置帧率
        Camera.Parameters parameters = mCamera.getParameters();
        mCameraPreviewFps = CameraUtilsOld2.chooseFixedPreviewFps(parameters, expectFps * 1000);
        parameters.setRecordingHint(true);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(getCameraDisplayOrientation(context, mCameraID));
        setCameraPreviewAndPicSize(context, holder, parameters);
    }

    private static void setCameraPreviewAndPicSize(final Context context, final SurfaceHolder holder, Camera.Parameters parameters) {
        // 设置预览大小，图片大小
        try {
            mCamera.autoFocus(null);

            final int customW = holder.getSurfaceFrame().width();
            final int customH = holder.getSurfaceFrame().height();
            mOrientation = CameraParamUtil.getInstance().getCameraDisplayOrientation(context, mCameraID);
            // 预览大小
            previewSize = CameraParamUtil.getInstance().calculatePerfectSize(parameters
                    .getSupportedPreviewSizes(), customW, customH, mOrientation);
            // 图片大小
            Camera.Size pictureSize = CameraParamUtil.getInstance().calculatePerfectSize(parameters
                    .getSupportedPictureSizes(), customW, customH, mOrientation);

            parameters.setPreviewSize(previewSize.width, previewSize.height);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);

            Log.d("better", String.format("想要大小：%s,%s", customW, customH));
            Log.d("better", String.format("预览大小：%s,%s", previewSize.width, previewSize.height));
            Log.d("better", String.format("图片大小：%s,%s", pictureSize.width, pictureSize.height));

            // 设置自动对焦
            if (CameraParamUtil.getInstance().isSupportedFocusMode(parameters.getSupportedFocusModes(), Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            // 设置相片质量
            if (CameraParamUtil.getInstance().isSupportedPictureFormats(parameters.getSupportedPictureFormats(), ImageFormat.JPEG)) {
                parameters.setPictureFormat(ImageFormat.JPEG);
                parameters.setJpegQuality(100);
            }
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(holder);  //SurfaceView
            mCamera.setDisplayOrientation(mOrientation);//浏览角度
            mCamera.setPreviewCallback(previewCallback); //每一帧回调
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e("better", "" + e.getMessage());
        }
    }

    /**
     * 根据ID打开相机
     *
     * @param cameraID
     * @param expectFps
     */
    public static void openCamera(final Context context, int cameraID, final SurfaceHolder holder, int expectFps) {
        if (mCamera != null) {
            throw new RuntimeException("camera already initialized!");
        }
        mCamera = Camera.open(cameraID);
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }
        mCameraID = cameraID;
        Camera.Parameters parameters = mCamera.getParameters();
        mCameraPreviewFps = CameraUtilsOld2.chooseFixedPreviewFps(parameters, expectFps * 1000);
        parameters.setRecordingHint(true);
        mCamera.setParameters(parameters);
        mCamera.setDisplayOrientation(mOrientation);
        setCameraPreviewAndPicSize(context, holder, parameters);
    }

    /**
     * 开始预览
     *
     * @param holder
     */
    public static void startPreviewDisplay(SurfaceHolder holder) {
        if (mCamera == null) {
            throw new IllegalStateException("Camera must be set when start preview");
        }
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换相机
     *
     * @param cameraID
     */
    public static void switchCamera(Context context, int cameraID, SurfaceHolder holder) {
        if (mCameraID == cameraID) {
            return;
        }
        mCameraID = cameraID;
        // 释放原来的相机
        releaseCamera();
        // 打开相机
        openCamera(context, cameraID, holder, CameraUtilsOld2.DESIRED_PREVIEW_FPS);
        // 打开预览
        startPreviewDisplay(holder);
    }

    /**
     * 释放相机
     */
    public static void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始预览
     */
    public static void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
            mCamera.setPreviewCallback(previewCallback);
        }
    }

    /**
     * 停止预览
     */
    public static void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
        }
    }

    /**
     * 拍照
     */
    public static void takePicture(Camera.ShutterCallback shutterCallback,
                                   Camera.PictureCallback rawCallback,
                                   Camera.PictureCallback pictureCallback) {
        if (mCamera != null) {
            mCamera.takePicture(shutterCallback, rawCallback, pictureCallback);
        }
    }

    /**
     * 获取照片大小
     *
     * @return
     */
    public static Camera.Size getPictureSize() {
        if (mCamera != null) {
            return mCamera.getParameters().getPictureSize();
        }
        return null;
    }

    /**
     * 获取预览大小
     *
     * @return
     */
    public static Camera.Size getPreviewSize() {
        return previewSize;
    }

    /**
     * 选择合适的FPS
     *
     * @param parameters
     * @param expectedThoudandFps 期望的FPS
     * @return
     */
    public static int chooseFixedPreviewFps(Camera.Parameters parameters, int expectedThoudandFps) {
        List<int[]> supportedFps = parameters.getSupportedPreviewFpsRange();
        for (int[] entry : supportedFps) {
            if (entry[0] == entry[1] && entry[0] == expectedThoudandFps) {
                parameters.setPreviewFpsRange(entry[0], entry[1]);
                return entry[0];
            }
        }
        int[] temp = new int[2];
        int guess;
        parameters.getPreviewFpsRange(temp);
        if (temp[0] == temp[1]) {
            guess = temp[0];
        } else {
            guess = temp[1] / 2;
        }
        return guess;
    }

    /**
     * 设置预览角度，setDisplayOrientation本身只能改变预览的角度
     * previewFrameCallback以及拍摄出来的照片是不会发生改变的，拍摄出来的照片角度依旧不正常的
     * 拍摄的照片需要自行处理
     * 这里Nexus5X的相机简直没法吐槽，后置摄像头倒置了，切换摄像头之后就出现问题了。
     */
    public static int getCameraDisplayOrientation(Context context, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = wm.getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;   // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }


    /**
     * 获取当前的Camera ID
     *
     * @return
     */
    public static int getCameraID() {
        return mCameraID;
    }

    public static Camera getCamera() {
        return mCamera;
    }

    /**
     * 获取当前预览的角度
     *
     * @return
     */
    public static int getPreviewOrientation() {
        return mOrientation;
    }

    /**
     * 获取FPS（千秒值）
     *
     * @return
     */
    public static int getCameraPreviewThousandFps() {
        return mCameraPreviewFps;
    }
}