package com.github.android.sample.camera.ImageSize;

import android.app.Activity;
import android.hardware.Camera;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 代码备份
 * Created by better On 2020-06-26.
 */
public class ChooseImageSizeUitls2 {

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
        Camera.Size result = sizes.get(0);
        int desiredWidth;
        int desiredHeight;
        final int surfaceWidth = expectWidth;
        final int surfaceHeight = expectHeight;
        if (isLandscape(displayOrientation)) {
            desiredWidth = surfaceHeight;
            desiredHeight = surfaceWidth;
        } else {
            desiredWidth = surfaceWidth;
            desiredHeight = surfaceHeight;
        }

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

        /*
        boolean widthOrHeight = false;  // 判断存在宽或高相等的Size
        // 辗转计算宽高最接近的值
        for (Camera.Size size : sizes) {
            // 如果宽高相等，则直接返回
            if (size.width == desiredWidth && size.height == desiredHeight) {
                result = size;
                break;
            }
            // 仅仅是宽度相等，计算高度最接近的size
            if (size.width == desiredWidth) {
                widthOrHeight = true;
                if (Math.abs(result.height - desiredHeight) > Math.abs(size.height - desiredHeight)) {
                    result = size;
                }
            }
            // 高度相等，则计算宽度最接近的Size
            else if (size.height == desiredHeight) {
                widthOrHeight = true;
                if (Math.abs(result.width - desiredWidth) > Math.abs(size.width - desiredHeight)) {
                    result = size;
                }
            }
            // 如果之前的查找不存在宽或高相等的情况，则计算宽度和高度都最接近的期望值的Size
            else if (!widthOrHeight) {
                // 选取宽高都比期望稍大的
                if (desiredWidth <= size.width && desiredHeight <= size.height) {
                    result = size;
                    break;
                }
            }
        }
         */

        /*
        后面考虑
        for (Camera.Size size : sizes) {// sizes 已按照从小到大排序
            // 选取宽高都比期望稍大的
            if (desiredWidth <= size.width && desiredHeight <= size.height) {
                result = size;
                break;
            }
        }
        */
        ////////////

        Log.d("better", "expect:" + expectWidth + ", " + expectHeight + "result:" + result.width + ", " + result.height);
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

    /**
     * Test if the supplied orientation is in landscape.
     *
     * @param orientationDegrees Orientation in degrees (0,90,180,270)
     * @return True if in landscape, false if portrait
     */
    private static boolean isLandscape(int orientationDegrees) {
        return (orientationDegrees == 90 ||
                orientationDegrees == 270);
    }

}
