package com.github.android.sample.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PhotoUtil {
    public static final String TAG = "PhotoUtil";

    public final static String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * 从协议中(content://) copy 文件
     * if oldPath is not begin with 'content://' then return oldPath
     */
    public static String copyImageFile(Context context, String oldPath) {
        String result = null;
        if (!TextUtils.isEmpty(oldPath)) {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = true;
            newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;

            if (oldPath.startsWith("content://")) {      // 获取输入流
                FileInputStream fis = null;
                try {
                    final FileDescriptor r = context.getContentResolver().openFileDescriptor(Uri.parse(oldPath), "r").getFileDescriptor();
                    BitmapFactory.decodeFileDescriptor(r, null, newOpts);
                    result = CACHE_PATH + "/photo/test.tmp." + System.currentTimeMillis() + (hasSuffix(newOpts) ? ".jpg" : ".png");
                    // 1. 拿到原始文件流，并获取文件名
                    fis = new FileInputStream(r);
                    // 2. 写入文件
                    if (copyFile(fis, result)) {
                        final String newFilePath = rotateImage(context, result);
                        return newFilePath;
                    }
                } catch (Exception e) {
                    result = null;
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                        }
                    }
                }

            } else {
                // absolute path
                try {
                    // 1.获取扩展名
                    BitmapFactory.decodeFile(oldPath, newOpts);
                    result = CACHE_PATH + "/photo/test.tmp." + System.currentTimeMillis() + (hasSuffix(newOpts) ? ".jpg" : ".png");
                    // 2.copy 文件
                    if (copyFile(oldPath, result)) {
                        final String newFilePath = rotateImage(context, result);
                        return newFilePath;
                    }
                } catch (Exception e) {
                    result = null;
                }
            }
        }


        // 经测试在Android 4.2 上  BitmapFactory.decodeFileDescriptor(r) 返回 null
//        if(result == null) {
//            return ImageUtil.getRealPathFromUrl(context, oldPath);
//        }
        return result;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
        }
        return degree;
    }

    public static boolean hasSuffix(BitmapFactory.Options options) {
        if (options == null) {
            return false;
        }
        String str = options.outMimeType;
        if (str == null) {
            return false;
        }
        str = str.toLowerCase();
        if (str.indexOf("jpg") >= 0) {
            return true;
        }
        if (str.indexOf("jpeg") >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取后缀
     *
     * @param options
     * @return
     */
    public static String getSuffix(BitmapFactory.Options options) {
        String str = options.outMimeType;
        if (str == null) {
            return "unknown";
        }
        str = str.toLowerCase();
        if (str.indexOf("jpg") >= 0) {
            return "jpeg";
        }
        if (str.indexOf("jpeg") >= 0) {
            return "jpeg";
        }
        if (str.indexOf("png") >= 0) {
            return "png";
        }
        if (str.indexOf("gif") >= 0) {
            return "gif";
        }
        return "unknown";
    }


    /**
     * 压缩图片,图片不超过512KB
     *
     * @param context
     * @param str     原图片路径
     */
    public static String compress(Context context, String str) {
        Bitmap decodeFile;
        try {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = true;
            newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeFile(str, newOpts);

            String str2 = CACHE_PATH + "/photo/mantoMsg.tmp" + System.currentTimeMillis() + (hasSuffix(newOpts) ? ".jpg" : ".png");

            //原图的宽高
            int w = newOpts.outWidth;
            int h = newOpts.outHeight;
            // 想要缩放的目标尺寸
            int wantWidth = context.getResources().getDisplayMetrics().widthPixels / 2;
            int wantHeight = context.getResources().getDisplayMetrics().heightPixels / 2;
            int be = 1;
            if (h > wantHeight || w > wantWidth) {
                final int heightRatio = Math.round((float) h / (float) wantHeight);
                final int widthRatio = Math.round((float) w / (float) wantWidth);
                be = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            if (be <= 0)
                be = 1;
            newOpts.inSampleSize = be;
            newOpts.inJustDecodeBounds = false;
            decodeFile = BitmapFactory.decodeFile(str, newOpts);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decodeFile.compress((hasSuffix(newOpts) ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG), 100, baos);
            int options = 100;
            while (baos.toByteArray().length / 1024 > 512) {    //循环判断如果压缩后图片是否大于512kb,大于继续压缩
                options -= 10;       // 每次都减少10
                if (options <= 0)   // 如果压缩不下去，则直接停止压缩。
                    break;
                baos.reset();       // 重置baos即清空baos
                decodeFile.compress((hasSuffix(newOpts) ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG), options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
            Bitmap resultBitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
            decodeFile.recycle();

            // 写入文件
            File file = new File(str2);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            resultBitmap.compress((hasSuffix(newOpts) ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG), 100, fos);
            fos.flush();
            fos.close();
            resultBitmap.recycle();
            return str2;
        } catch (OutOfMemoryError e) {
            decodeFile = null;
        } catch (NullPointerException e4) {
            decodeFile = null;
        } catch (Exception e322) {
            decodeFile = null;
        }
        if (decodeFile == null) {
            return null;
        }
        return str;
    }

    public static String rotateImage(Context context, String str) { // qy
        int orientationInDegree = PhotoUtil.getBitmapDegree(str);
        if (orientationInDegree == 0) {
            return str;
        }
        OutputStream os = null;
        orientationInDegree %= 360;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
            if (decodeFile == null) {
                return str;
            }

            Matrix matrix = new Matrix();
            matrix.reset();
            matrix.setRotate(orientationInDegree, decodeFile.getWidth() / 2, decodeFile.getHeight() / 2);
            Bitmap bmp = Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
            decodeFile.recycle();  // old recycle

            String str2 = CACHE_PATH + "/photo/mantoMsg." + System.currentTimeMillis() + (bmp.hasAlpha() ? ".png" : ".jpg");
            File file = new File(str2);
            if (file.getParentFile() != null && file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            os = new FileOutputStream(file);
            bmp.compress(bmp.hasAlpha() ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, os);
            bmp.recycle();
            return str2;
        } catch (OutOfMemoryError e3) {
            return str;
        } catch (NullPointerException e4) {
            return str;
        } catch (IOException e) {
            return str;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    return str;
                }
            }
        }
    }

    public static boolean copyFile(String src, String dest) {
        if (TextUtils.isEmpty(src) || TextUtils.isEmpty(dest)) {
            return false;
        } else if (src.equals(dest)) {
            return true;
        } else {
            boolean result = true;
            FileInputStream ins = null;
            FileOutputStream ous = null;
            try {
                File file = new File(dest);
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                ins = new FileInputStream(src);
                ous = new FileOutputStream(dest);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try {
                byte[] buffer = new byte[10240];
                while (true) {
                    int read = ins.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    ous.write(buffer, 0, read);
                }
            } catch (IOException e) {
                result = false;
            } finally {
                try {
                    ins.close();
                    ous.close();

                } catch (Exception e) {
                }
                return result;
            }
        }
    }

    public static boolean copyFile(InputStream in, String dest) {
        if(in == null) {
            return false;
        }
        boolean result = true;
        FileOutputStream ous = null;
        try {
            File file = new File(dest);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            ous = new FileOutputStream(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            byte[] buffer = new byte[10240];
            while (true) {
                int read = in.read(buffer);
                if (read == -1) {
                    break;
                }
                ous.write(buffer, 0, read);
            }
        } catch (IOException e) {
            result = false;
        } finally {
            try {
                in.close();
                ous.close();

            } catch (Exception e) {
            }
            return result;
        }
    }
}
