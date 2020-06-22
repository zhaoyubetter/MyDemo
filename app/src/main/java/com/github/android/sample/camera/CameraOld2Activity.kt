package com.github.android.sample.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.widget.FrameLayout
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_camera_old.btn_switch
import kotlinx.android.synthetic.main.activity_camera_old2.*
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * 可自定义预览大小，并且不会失真，变型
 *
 * 打开此Activity时，需要先获取权限
 */
class CameraOld2Activity : ToolbarActivity() {


    /**
     * 封装了 Camera
     */
    private var cameraSurfaceView: CameraSurfaceView? = null
    private lateinit var mAspectLayout: FrameLayout
    /**
     * camera 方向
     */
    private var mOrientation = 0


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
        val width = resources.displayMetrics.widthPixels / 2
        val height = resources.displayMetrics.heightPixels / 2
        mAspectLayout.addView(cameraSurfaceView, width, height)
        mOrientation = CameraUtilsOld2.getCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_BACK)

        // 拍照
        btn_take.setOnClickListener {
            CameraUtilsOld2.takePicture(Camera.ShutterCallback { }, null, object : Camera.PictureCallback {
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
}
