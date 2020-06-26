package com.github.android.sample.camera.ImageSize;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Size;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by better On 2020-06-26.
 */
public class ChooseImageSizeUitls1 {

    public static Camera.Size chooseOptimalSize(Activity activity, int exceptWidth, int exceptHeight, List<Camera.Size> sizes) {
        sortList(sizes); // 根据宽度进行排序
        int desiredWidth;
        int desiredHeight;
        final int surfaceWidth = exceptWidth;
        final int surfaceHeight = exceptHeight;

        int mDisplayOrientation = activity.getWindowManager().getDefaultDisplay().getRotation();
        if (isLandscape(mDisplayOrientation)) {
            desiredWidth = surfaceHeight;
            desiredHeight = surfaceWidth;
        } else {
            desiredWidth = surfaceWidth;
            desiredHeight = surfaceHeight;
        }
        Camera.Size result = null;
        for (Camera.Size size : sizes) {
            if (desiredWidth <= size.width && desiredHeight <= size.height) {
                return size;
            }
            result = size;
        }
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

    public static AspectRatio getDeviceAspectRatio(Activity activity) {
        int width = activity.getWindow().getDecorView().getWidth();
        int height = activity.getWindow().getDecorView().getHeight();
        return AspectRatio.of(height, width);
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
