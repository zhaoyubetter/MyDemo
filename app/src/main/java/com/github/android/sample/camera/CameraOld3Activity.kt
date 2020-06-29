package com.github.android.sample.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import com.github.android.sample.camera.CameraParamUtil.isLandscape
import com.google.android.cameraview.CameraView
import kotlinx.android.synthetic.main.activity_camera_old2.*
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException


/**
 * 使用 google 封装的，自己做的
 * 相片最大大小，不要超过 1920 * 1080
 */
class CameraOld3Activity : ToolbarActivity() {

    val TAG = "better"

    /**
     * 封装了 Camera
     */
    private lateinit var mAspectLayout: FrameLayout
    private lateinit var maskView: MaskView

    private lateinit var mCameraView: CameraView

    private var mBackgroundHandler: Handler? = null

    private var usePreviewRect = false

    val DST_CENTER_RECT_WIDTH = 200
    val DST_CENTER_RECT_HEIGHT = 200

    private var rectPictureSize: Point? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_old3)
        initView()
    }

    override fun onPause() {
        mCameraView.stop()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) === PackageManager.PERMISSION_GRANTED) {
            mCameraView.start()
        }
        maskView.post {
            maskView?.setCenterRect(createPicRect(mCameraView, DST_CENTER_RECT_WIDTH * 3, DST_CENTER_RECT_HEIGHT * 3))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler?.getLooper()?.quitSafely()
            } else {
                mBackgroundHandler?.getLooper()?.quit()
            }
            mBackgroundHandler = null
        }
    }

    private val mCallback: CameraView.Callback = object : CameraView.Callback() {
        override fun onCameraOpened(cameraView: CameraView) {
            Log.d(TAG, "onCameraOpened")
        }

        override fun onCameraClosed(cameraView: CameraView) {
            Log.d(TAG, "onCameraClosed")
        }

        override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
            Log.d(TAG, "onPictureTaken " + data.size)
            getBackgroundHandler()?.post {

                val path = applicationContext.externalCacheDir?.absolutePath + System.currentTimeMillis() + ".jpg"
                try {
                    var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    // 1.保存原始的图片
                    saveOriginPic(bitmap)
                    // 2.保存预览框中的图片
                    createRectPic(bitmap)

                    if (!bitmap.isRecycled) {
                        bitmap.recycle()
                    }
                } catch (e: IOException) {
                    Log.w(TAG, "Cannot write to $path", e)
                } finally {

                }
            }
        }
    }

    /**
     * 所见即所得的图片做法
     */
    private fun saveOriginPic(originalBitmap: Bitmap) {
        // 获取角度
        val orientation = CameraUtilsOld2.getCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_BACK)
        var nowAngle = 0
        // 1.判断角度
        when (orientation) {
            90 -> nowAngle = Math.abs(orientation) % 360
            270 -> nowAngle = Math.abs(orientation)
        }
        // 2.判断摄像头
        if (mCameraView.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            nowAngle = nowAngle
        } else if (mCameraView.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            nowAngle = 360 - nowAngle
        }
        // 旋转一下原始图片
        val originalBitmap = ImageUtils.getRotatedBitmap(originalBitmap, nowAngle)

        var toSaveBitmap = originalBitmap
        // 原始图片，这里保存原始图片，如果高度设置很小，那么这里的图片也会是全部高度
        val path = applicationContext.externalCacheDir?.absolutePath + "original_" + System.currentTimeMillis() + ".jpg"
        // 巨坑，这里不同机型截图图片会有问题：
        // 可能实际图片 width 2440,而获取的屏幕宽 1080,那么直接拿 1080 去截取，那么截图必错，我们需要分为2部分来做：
        //  1. 当前图片的比例 ratio，自己肯定知道，假设是 3:4
        //  2. 屏幕的宽高，你肯定知道，假设是 1080 * 1920，一般拍摄图片，宽都是屏幕宽，高可能用户指定了；
        //  3. 假设你想要的图片大小是：1080 * 900，实际上，拍出来的图片可能是 (2976*3968)，拍的图片不可能你想要多大，就多大的；
        // 那怎么办？我确实只想要 1080 * 900，普通的做法，就是直接给图片，缩放一下，但是包含了你不想要的图片部分，我们尝试另一个做法，这个iOS做的很好
        //  1. 假设拍照时，横向铺满（width: 1080），如果拍出的照片是width是 1080，那么皆大欢喜，直接截图height为 900 即可，如果不是呢，麻烦事情来了？
        // 我们假设图片大小为(2976*3968)，横向因为铺满，这好办，直接给 2976，那么高度的截取要这么操作：
        //  a. 横向 1080 (2976)，ratio 3:4，我们得出竖向的实际高度为 1080/(3/4) = 1440，注意：拍照的出来的图片与预览的图片会有差距；
        //  b. 我们拿 3968 * 1.0f / 1440 得到比例：2.75，然后拿 2.75 * 900 得到 2480，这就是正确的位置；
        //     计算方法：3968 * 1.0f / 1440 * 900
        // 特别注意：一般没有特殊要求，不要使用此方法，很容易发生意外，而采用原图提供；
        // 对于截图的开始的 x/y，一般都是 0,0，如有特殊，也需要按此方案解决

        // 判断下方向
        if (originalBitmap.height > mCameraView.height) {
            val showHeight = (mCameraView.width * 1.0f / (mCameraView.aspectRatio!!.inverse()!!.toFloat())).toInt()
            val realCutHeight = (originalBitmap.height * 1.0f / showHeight) * mCameraView.height
            val toSavePicSize = createPicPoint(originalBitmap, originalBitmap.width, realCutHeight.toInt())
            Log.i("better", "original.getWidth() = " + originalBitmap.width + " original.getHeight() = " + originalBitmap.height)
            toSaveBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, toSavePicSize!!.x, toSavePicSize!!.y)
        }

        val fout = FileOutputStream(path)
        val bos = BufferedOutputStream(fout)
        toSaveBitmap?.compress(Bitmap.CompressFormat.JPEG, 75, bos)
        bos.flush()
        bos.close()
        fout.close()
    }


    /**生成拍照后图片的中间矩形的宽度和高度
     * @param originalBitmap 原始图片
     * @param w 屏幕上的矩形宽度，单位px
     * @param h 屏幕上的矩形高度，单位px
     * @return
     */
    private fun createPicPoint(originalBitmap: Bitmap, w: Int, h: Int): Point? {
        // 原始图片 size，也有可能是屏幕大小，如果是屏幕，这里需要改写
        val wScreen: Int = originalBitmap.width
        val hScreen: Int = originalBitmap.height

        // 保存图片的宽高，也就是 pictureSize
        val wSavePicture: Int = originalBitmap.width
        val hSavePicture: Int = originalBitmap.height
        val wRate = wSavePicture.toFloat() / wScreen.toFloat()
        val hRate = hSavePicture.toFloat() / hScreen.toFloat()
        val rate = if (wRate <= hRate) wRate else hRate //也可以按照最小比率计算
        val wRectPicture = (w * wRate).toInt()
        val hRectPicture = (h * hRate).toInt()
        return Point(wRectPicture, hRectPicture)
    }

    // 预览框图片
    private fun createRectPic(originalBitmap: Bitmap) {
        if (!usePreviewRect) {
            return
        }

        if (rectPictureSize == null) {
            rectPictureSize = createPicPoint(originalBitmap, 3 * DST_CENTER_RECT_WIDTH, 3 * DST_CENTER_RECT_HEIGHT)
        }
        val path = applicationContext.externalCacheDir?.absolutePath + "_rect_" + System.currentTimeMillis() + ".jpg"
        val x: Int = originalBitmap.width / 2 - rectPictureSize!!.x / 2
        val y: Int = originalBitmap.height / 2 - rectPictureSize!!.y / 2
        Log.i("better", "original.getWidth() = " + originalBitmap.width + " original.getHeight() = " + originalBitmap.height)
        // 截取图片
        var rectBitmap = Bitmap.createBitmap(originalBitmap, x, y, rectPictureSize!!.x, rectPictureSize!!.y)
        Log.i("better", "target.getWidth() = " + rectBitmap.width + " target.getHeight() = " + rectBitmap.height)

        try {
            val fout = FileOutputStream(path)
            val bos = BufferedOutputStream(fout)
            rectBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
            fout.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (rectBitmap!!.isRecycled) {
                rectBitmap.recycle()
            }
        }
    }

    private fun initView() {
        mCameraView = findViewById(R.id.camera)
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback)
        }
        mAspectLayout = findViewById(R.id.layout_aspect)
        // 遮罩
        maskView = findViewById(R.id.maskView)
        maskView.bringToFront()

        if (!usePreviewRect) {
            maskView.visibility = View.INVISIBLE
        }

        btn_take.setOnClickListener {
            mCameraView.takePicture()
        }
    }

    private fun getBackgroundHandler(): Handler? {
        if (mBackgroundHandler == null) {
            val thread = HandlerThread("background")
            thread.start()
            mBackgroundHandler = Handler(thread.looper)
        }
        return mBackgroundHandler
    }

    /**
     * 生成预览框矩形
     * @param v cameraView
     * @param w     目标矩形的宽度,单位px
     * @param h     目标矩形的高度,单位px
     * @return
     */
    private fun createPicRect(v: View, w: Int, h: Int): Rect? {
        val x1: Int = v.width / 2 - w / 2
        val y1: Int = v.height / 2 - h / 2
        val x2 = x1 + w
        val y2 = y1 + h
        return Rect(x1, y1, x2, y2)
    }
}
