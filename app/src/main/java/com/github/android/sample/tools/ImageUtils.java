package com.github.android.sample.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

/**
 * https://juejin.cn/post/6844904096541966350#heading-86
 * @author zhaoyu1  2021/1/13
 **/
public final class ImageUtils {

    static final String TAG = "ImageUtils";

    public static Bitmap getOriginBitmap(final Context context, final int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeResource(context.getResources(), resId, options);

        // The original
        int w = options.outWidth;
        int h = options.outHeight;
        // 这个算法不对，1个像素点消耗32bit，共4个字节，那么消耗内存为：
        Log.d(TAG, String.format("width:%s,height:%s, OriginBitmap cost : %s, --> %s", w, h,
                w * h * 4, Formatter.formatFileSize(context, w * h * 4)));


        options.inJustDecodeBounds = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        Log.d(TAG, String.format("bitmap.getByteCount() ==> %s", bitmap.getByteCount()));

        // width/文件夹密度 * 设备实际密度 * height/文件夹密度 * 设备实际密度 * 4
        // 522/480 * 640 * 686/480 *640 * 4 = 2546432B
        final float targetDensity = context.getResources().getDisplayMetrics().densityDpi;
        Log.d(TAG, String.format("正确的算法 ==> %s", w / 480f * targetDensity * h / 480f * targetDensity * 4));
        return bitmap;
    }

    public static Bitmap getScaleBitmap(final Context context, final int resId, final View view) {
        final int reqWidth = view.getWidth();
        final int reqHeight = view.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeResource(context.getResources(), resId, options);

        final int sampleSize = calculateInSampleSize2(options, reqWidth, reqHeight);
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        final Bitmap result = BitmapFactory.decodeResource(context.getResources(), resId, options);
        Log.d(TAG, String.format("sampleSize: %s, cost:%s", sampleSize, result.getByteCount()));
        return result;
    }

    /**
     * 系统自动匹配，经测试，生成的图片稍大于 getScaleBitmap() 方法
     * @param context
     * @param resId
     * @param view
     * @return
     */
    public static Bitmap getScaleBitmapAuto(final Context context, final int resId, final View view) {
        final int reqWidth = view.getWidth();
        final int reqHeight = view.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeResource(context.getResources(), resId, options);

        final int sampleSize = calculateInSampleSize2(options, reqWidth, reqHeight);
        options.inSampleSize = sampleSize;
        options.inScaled = true;
        options.inDensity = options.outWidth;
        options.inTargetDensity = reqWidth * options.inSampleSize;
        options.inJustDecodeBounds = false;

        final Bitmap result = BitmapFactory.decodeResource(context.getResources(), resId, options);
        Log.d(TAG, String.format("sampleSize: %s, cost:%s", sampleSize, result.getByteCount()));
        return result;
    }

    /**
     * 推荐使用这个
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        // 想要缩放的目标尺寸
        int wantWidth = reqWidth;
        int wantHeight = reqHeight;
        int be = 1;
        if (height > wantHeight || width > wantWidth) {
            final int heightRatio = Math.round((float) width / (float) wantHeight);
            final int widthRatio = Math.round((float) width / (float) wantWidth);
            be = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        if (be <= 0)
            be = 1;
        return be;
    }

    // google 方案
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
