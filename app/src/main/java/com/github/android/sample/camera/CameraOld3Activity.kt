package com.github.android.sample.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.Rect
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
        var toSaveBitmap = originalBitmap
        // 原始图片，这里保存原始图片，如果高度设置很小，那么这里的图片也会是全部高度
        val path = applicationContext.externalCacheDir?.absolutePath + "original_" + System.currentTimeMillis() + ".jpg"
        if (originalBitmap.height > mCameraView.height) {        // 截图图片
            val toSavePicSize = createPicPoint(originalBitmap, originalBitmap.width, mCameraView.height)
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

    private fun getBackgroundHandler(): Handler? {
        if (mBackgroundHandler == null) {
            val thread = HandlerThread("background")
            thread.start()
            mBackgroundHandler = Handler(thread.looper)
        }
        return mBackgroundHandler
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

        // 保存图片的宽高
        val wSavePicture: Int = originalBitmap.width
        val hSavePicture: Int = originalBitmap.height
        val wRate = wSavePicture.toFloat() / wScreen.toFloat()
        val hRate = hSavePicture.toFloat() / hScreen.toFloat()
        val rate = if (wRate <= hRate) wRate else hRate //也可以按照最小比率计算
        val wRectPicture = (w * wRate).toInt()
        val hRectPicture = (h * hRate).toInt()
        return Point(wRectPicture, hRectPicture)
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
