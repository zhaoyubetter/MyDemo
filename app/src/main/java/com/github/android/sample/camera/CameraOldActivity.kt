package com.github.android.sample.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_camera_old.*
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors


/**
 * 进入此Activity必须申请好了权限
 */
class CameraOldActivity : AppCompatActivity(), SurfaceHolder.Callback, Camera.PreviewCallback {
    // 使用 SurfaceView 来预览
    private lateinit var surfaceView: SurfaceView
    private var mOrientation: Int = 0
    private var collectionPreviewCount = 0
    private val executor = Executors.newFixedThreadPool(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_old)
        initView()

        // 一些拍照代码，是自己创建SurfaceView自定义类，包装了 SurfaceView
        surfaceView = findViewById(R.id.surfaceView)
        surfaceView.holder.addCallback(this)
        CameraUtils.previewCallback = this      // camera 实时预览
    }

    private fun initView() {
        mOrientation = CameraUtils.calculateCameraPreviewOrientation(this)

        // 拍照
        btn_capture.setOnClickListener {
            CameraUtils.takePicture(Camera.ShutterCallback { }, null, object : Camera.PictureCallback {
                override fun onPictureTaken(data: ByteArray, camera: Camera) {
                    var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    if (bitmap != null) {
                        bitmap = ImageUtils.getRotatedBitmap(bitmap, mOrientation)
                        val path = applicationContext.externalCacheDir?.absolutePath + System.currentTimeMillis() + ".jpg"
                        try {
                            val fout = FileOutputStream(path)
                            val bos = BufferedOutputStream(fout)
                            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                            bos.flush()
                            bos.close()
                            fout.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                    // 拍完继续预览
                    CameraUtils.startPreview()
                }
            })
        }

        // 切换相机
        btn_switch.setOnClickListener {
            if (surfaceView != null) {
                CameraUtils.switchCamera(1 - CameraUtils.getCameraID(), surfaceView.holder);
                // 切换相机后需要重新计算旋转角度
                mOrientation = CameraUtils.calculateCameraPreviewOrientation(this)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        CameraUtils.stopPreview()
    }

    override fun onResume() {
        super.onResume()
        collectionPreviewCount = 0
        CameraUtils.startPreview()
    }

    // camera 预览帧数据回调
    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        val parameters = camera?.parameters
        val size = parameters?.previewSize
        val width = size?.width // 竖屏是反的
        val height = size?.height // 竖屏是反的

        /*
        // 保存生成的预览图片
        collectionPreviewCount++
        executor.submit {
            val fileName = applicationContext.externalCacheDir?.absolutePath + collectionPreviewCount + "_" + System.currentTimeMillis() + ".jpg"
            ImageUtils.savePreviewBitmap(data, camera, fileName)
        }
        */
    }

    // =========== SurfaceHolder.Callback 回调 ====================
    // surfaceCreated 由系统自动调用
    override fun surfaceCreated(holder: SurfaceHolder?) {
        // === 1. 打开Camera
        CameraUtils.openBackCamera(CameraUtils.DESIRED_PREVIEW_FPS)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        // === 2.开启 Camera 预览
        CameraUtils.startPreviewDisplay(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        // === 3.Camera 释放
        CameraUtils.releaseCamera()
    }
    // =========== SurfaceHolder.Callback 回调 ====================
}
