package com.github.android.sample.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 推荐使用
 */
public final class CameraParamUtil {
    private static final String TAG = "better";
    private static CameraParamUtil cameraParamUtil = null;

    private CameraParamUtil() {

    }

    public static CameraParamUtil getInstance() {
        if (cameraParamUtil == null) {
            cameraParamUtil = new CameraParamUtil();
            return cameraParamUtil;
        } else {
            return cameraParamUtil;
        }
    }

    /**
     * 计算最完美的Size
     *
     * @param sizes
     * @param expectWidth
     * @param expectHeight
     * @param displayOrientation 相机角度
     * @return
     */
    public static Camera.Size calculatePerfectSize(List<Camera.Size> sizes, int expectWidth, int expectHeight, int displayOrientation) {
        sortList(sizes); // 根据宽度进行排序
        Camera.Size result = null;
        int desiredWidth = expectWidth;
        int desiredHeight = expectHeight;
        final int surfaceWidth = expectWidth;
        final int surfaceHeight = expectHeight;
//        if (isLandscape(displayOrientation)) {
//            desiredWidth = surfaceHeight;
//            desiredHeight = surfaceWidth;
//        } else {
//            desiredWidth = surfaceWidth;
//            desiredHeight = surfaceHeight;
//        }

        for (Camera.Size size : sizes) {// sizes 已按照从小到大排序
            // 选取宽高都比期望稍大的
            if (desiredWidth <= size.width && desiredHeight <= size.height) {
                result = size;
                break;
            }
        }

        if(result == null) {
            // Use a very small tolerance because we want an exact match.
            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) desiredWidth / desiredHeight;
            if (sizes == null)
                return null;

            Camera.Size optimalSize = null;

            // Start with max value and refine as we iterate over available preview sizes. This is the
            // minimum difference between view and camera height.
            double minDiff = Double.MAX_VALUE;

            // Target view height
            int targetHeight = desiredHeight;

            // Try to find a preview size that matches aspect ratio and the target view size.
            // Iterate over all available sizes and pick the largest size that can fit in the view and
            // still maintain the aspect ratio.
            for (Camera.Size size : sizes) {
                double ratio = (double) size.width / size.height;
                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                    continue;
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }

            // Cannot find preview size that matches the aspect ratio, ignore the requirement
            if (optimalSize == null) {
                minDiff = Double.MAX_VALUE;
                for (Camera.Size size : sizes) {
                    if (Math.abs(size.height - targetHeight) < minDiff) {
                        optimalSize = size;
                        minDiff = Math.abs(size.height - targetHeight);
                    }
                }
            }

            result = optimalSize;
        }

        Log.d(TAG, "expect:" + expectWidth + ", " + expectHeight + "result:" + result.width + ", " + result.height);
        return result;
    }


    /**
     * 排序
     *
     * @param list
     */
    private static void sortList(List<Camera.Size> list) {
        Collections.sort(list, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size pre, Camera.Size after) {
                if (pre.width > after.width) {
                    return 1;
                } else if (pre.width < after.width) {
                    return -1;
                }
                return 0;
            }
        });
    }

    public boolean isSupportedFocusMode(List<String> focusList, String focusMode) {
        for (int i = 0; i < focusList.size(); i++) {
            if (focusMode.equals(focusList.get(i))) {
                return true;
            }
        }
        Log.i(TAG, "FocusMode not supported " + focusMode);
        return false;
    }

    public boolean isSupportedPictureFormats(List<Integer> supportedPictureFormats, int jpeg) {
        for (int i = 0; i < supportedPictureFormats.size(); i++) {
            if (jpeg == supportedPictureFormats.get(i)) {
                return true;
            }
        }
        return false;
    }

    public int getCameraDisplayOrientation(Context context, int cameraId) {
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
     * Test if the supplied orientation is in landscape.
     *
     * @param orientationDegrees Orientation in degrees (0,90,180,270)
     * @return True if in landscape, false if portrait
     */
    public static boolean isLandscape(int orientationDegrees) {
        return (orientationDegrees == 90 ||
                orientationDegrees == 270);
    }
}
