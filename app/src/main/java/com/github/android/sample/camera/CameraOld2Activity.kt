package com.github.android.sample.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_camera_old.btn_switch
import kotlinx.android.synthetic.main.activity_camera_old2.*
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

/**
 * 可自定义预览大小，并且不会失真，变型
 *
 * 打开此Activity时，需要先获取权限
 */
class CameraOld2Activity : ToolbarActivity(), Camera.PreviewCallback  {


    /**
     * 封装了 Camera
     */
    private var cameraSurfaceView: CameraSurfaceView? = null
    private lateinit var mAspectLayout: FrameLayout

    /**
     * camera 方向
     */
    private var mOrientation = 0

    private val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_old2)
        initView()
    }

    override fun onPause() {
        super.onPause()
        CameraUtilsOld2.stopPreview()
    }

    override fun onResume() {
        super.onResume()
        CameraUtilsOld2.startPreview()
    }

    private fun initView() {
        mAspectLayout = findViewById(R.id.layout_aspect)
        cameraSurfaceView = CameraSurfaceView(this)
        cameraSurfaceView?.setCameraPreviewCallback(this)
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels / 2
        mAspectLayout.addView(cameraSurfaceView, width, height)

        Log.d("better", "user want size, width:$width, height:$height")
        mOrientation = CameraUtilsOld2.getCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_BACK)

        // 拍照
        btn_take.setOnClickListener {
            CameraUtilsOld2.takePicture(Camera.ShutterCallback { }, null, object : Camera.PictureCallback {
                override fun onPictureTaken(data: ByteArray, camera: Camera) {
                    var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    if (bitmap != null) {
                        var nowAngle = 0
                        // 1.判断角度
                        when (mOrientation) {
                            90 -> nowAngle = Math.abs(mOrientation) % 360
                            270 -> nowAngle = Math.abs(mOrientation)
                        }
                        // 2.判断摄像头
                        if (CameraUtilsOld2.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            nowAngle = nowAngle
                        } else if (CameraUtilsOld2.getCameraID() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            nowAngle = 360 - nowAngle
                        }

                        bitmap = ImageUtils.getRotatedBitmap(bitmap, nowAngle)
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
                    CameraUtilsOld2.startPreview()
                }
            })
        }

        // 切换相机
        btn_switch.setOnClickListener {
            if (cameraSurfaceView != null) {
                val cameraId = if (CameraUtilsOld2.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    Camera.CameraInfo.CAMERA_FACING_FRONT
                } else {
                    Camera.CameraInfo.CAMERA_FACING_BACK
                }
                CameraUtilsOld2.switchCamera(applicationContext, cameraId, cameraSurfaceView?.holder)
                // 切换相机后需要重新计算旋转角度
                mOrientation = CameraUtilsOld2.getPreviewOrientation()
            }
        }
    }

    private var collectionPreviewCount = 0;

    // camera 预览帧数据回调
    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        val parameters = camera?.parameters
        val size = parameters?.previewSize
        val width = size?.width // 竖屏是反的
        val height = size?.height // 竖屏是反的

        var nowAngle = 0
        // 1.判断角度
        when (mOrientation) {
            90 -> nowAngle = Math.abs(mOrientation) % 360
            270 -> nowAngle = Math.abs(mOrientation)
        }
        // 2.判断摄像头
        if (CameraUtilsOld2.getCameraID() == Camera.CameraInfo.CAMERA_FACING_BACK) {
            nowAngle = nowAngle
        } else if (CameraUtilsOld2.getCameraID() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            nowAngle = 360 - nowAngle
        }

        // 保存生成的预览图片
        collectionPreviewCount++
        executor.submit {
            val fileName = applicationContext.externalCacheDir?.absolutePath + collectionPreviewCount + "_" + System.currentTimeMillis() + ".jpg"
            ImageUtils.savePreviewBitmap(data, camera, fileName, nowAngle)
        }

    }

}
