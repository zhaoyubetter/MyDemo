package com.github.android.sample.camera

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.CameraDevice.StateCallback
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_camera_main.*

/**
 * https://www.jianshu.com/p/73fed068a795
 * https://www.jianshu.com/nb/11632750
 */
@RequiresApi(Build.VERSION_CODES.M)
class CameraMainActivity : ToolbarActivity() {


    private val cameraManager: CameraManager by lazy { getSystemService(CameraManager::class.java) }
    private lateinit var cameraPreview: TextureView
    private var previewSurfaceTexture: SurfaceTexture? = null
    private var cameraCharacteristics: CameraCharacteristics? = null
    private var previewSurface: Surface? = null
    private var previewDataSurface: Surface? = null
    private var captureSession: CameraCaptureSession? = null


    //照片读取器
    private var previewDataImageReader: ImageReader? = null

    private lateinit var backCameraId: String

    // Camera 设备类
    private var cameraDevice: CameraDevice? = null

    companion object {
        private const val REQUEST_PERMISSION_CODE: Int = 1
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_main)
        checkRequiredPermissions()
        initView()

        // 遍历所有可用的摄像头 ID，只取出其中的前置和后置摄像头信息。
        val cameraIdList = cameraManager.cameraIdList
        cameraIdList.forEach { cameraId ->
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
            // use back camera
            if (cameraCharacteristics!![CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK) {
                backCameraId = cameraId
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
            }
        }
        cameraPreview.surfaceTextureListener = PreviewSurfaceTextureListener()
    }

    private fun createSession() {
        val sessionStateCallback = SessionStateCallback()
        val outputs = mutableListOf<Surface>()
        val previewSurface = previewSurface
        val previewDataSurface = previewDataSurface
        outputs.add(previewSurface!!)
        if (previewDataSurface != null) {
            outputs.add(previewDataSurface)
        }
        cameraDevice?.createCaptureSession(outputs, sessionStateCallback, null)
    }

    /**
     *  CaptureRequest可以完全自定义拍摄参数,但是需要配置的参数太多了,所以Camera2提供了一些快速配置的参数,如下:
    // 　　　　　　　　　      TEMPLATE_PREVIEW ：预览
    //                        TEMPLATE_RECORD：拍摄视频
    //                        TEMPLATE_STILL_CAPTURE：拍照
    //                        TEMPLATE_VIDEO_SNAPSHOT：创建视视频录制时截屏的请求
    //                        TEMPLATE_ZERO_SHUTTER_LAG：创建一个适用于零快门延迟的请求。在不影响预览帧率的情况下最大化图像质量。
    //                        TEMPLATE_MANUAL：创建一个基本捕获请求，这种请求中所有的自动控制都是禁用的(自动曝光，自动白平衡、自动焦点)。
     */
    private fun startPreview() {
        val cameraDevice = cameraDevice
        val captureSession = captureSession
        if (cameraDevice != null && captureSession != null) {
            val requestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            val previewSurface = previewSurface
            val previewDataSurface = previewDataSurface
            requestBuilder.addTarget(previewSurface!!)
            if (previewDataSurface != null) {
                requestBuilder.addTarget(previewDataSurface)
            }
            val request = requestBuilder.build()
            captureSession.setRepeatingRequest(request, RepeatingCaptureStateCallback(), null)
        }
    }

    private fun setPreviewSize() {
        val cameraCharacteristics = cameraCharacteristics
        if (cameraCharacteristics != null && previewSurfaceTexture != null) {
            // Get optimal preview size according to the specified max width and max height.
            val maxWidth = 1080
            val maxHeight = 1920
            val previewSize = getOptimalSize(cameraCharacteristics, SurfaceTexture::class.java, maxWidth, maxHeight)!!

            // Set the SurfaceTexture's buffer size with preview size.
            previewSurfaceTexture?.setDefaultBufferSize(previewSize.width, previewSize.height)
            previewSurface = Surface(previewSurfaceTexture)

            // Set up an ImageReader to receive preview frame data if YUV_420_888 is supported.
            val imageFormat = ImageFormat.YUV_420_888
            val streamConfigurationMap = cameraCharacteristics[CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]
            if (streamConfigurationMap?.isOutputSupportedFor(imageFormat) == true) {
                //创建图片读取器,参数为分辨率宽度和高度/图片格式/需要缓存几张图片,我这里写的3意思是获取3张照片
                previewDataImageReader = ImageReader.newInstance(previewSize.width, previewSize.height, imageFormat, 3)
                previewDataImageReader?.setOnImageAvailableListener(OnPreviewDataAvailableListener(), null)
                previewDataSurface = previewDataImageReader?.surface
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initView() {
        cameraPreview = findViewById<TextureView>(R.id.camera_preview)
        btn_open_back.setOnClickListener {
            cameraManager.openCamera(backCameraId, object : StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    setPreviewSize()
                    createSession()
                    startPreview()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    cameraDevice?.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    cameraDevice?.close()
                    cameraDevice = null
                }
            }, null)
        }
    }

    override fun onPause() {
        super.onPause()
        cameraDevice?.close()
    }

    private inner class RepeatingCaptureStateCallback : CameraCaptureSession.CaptureCallback() {
        @MainThread
        override fun onCaptureStarted(session: CameraCaptureSession, request: CaptureRequest, timestamp: Long, frameNumber: Long) {
            super.onCaptureStarted(session, request, timestamp, frameNumber)
        }

        @MainThread
        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            super.onCaptureCompleted(session, request, result)
        }
    }

    private inner class SessionStateCallback : CameraCaptureSession.StateCallback() {
        @MainThread
        override fun onConfigureFailed(session: CameraCaptureSession) {

        }

        @MainThread
        override fun onConfigured(session: CameraCaptureSession) {
            captureSession = session
            startPreview()
        }

        @MainThread
        override fun onClosed(session: CameraCaptureSession) {
            captureSession = null
        }
    }

    private inner class PreviewSurfaceTextureListener : TextureView.SurfaceTextureListener {
        @MainThread
        override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, width: Int, height: Int) = Unit

        @MainThread
        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) = Unit

        @MainThread
        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean = false

        @MainThread
        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
            previewSurfaceTexture = surfaceTexture
        }
    }

    private inner class OnPreviewDataAvailableListener : ImageReader.OnImageAvailableListener {
        /**
         * Called every time the preview frame data is available.
         */
        override fun onImageAvailable(imageReader: ImageReader) {
            // image.acquireLatestImage();//从ImageReader的队列中获取最新的image,删除旧的
            // image.acquireNextImage();//从ImageReader的队列中获取下一个图像,如果返回null没有新图像可用
            val image = imageReader.acquireNextImage()
            if (image != null) {
                val planes = image.planes
                val yPlane = planes[0]
                val uPlane = planes[1]
                val vPlane = planes[2]
                val yBuffer = yPlane.buffer // Data from Y channel
                val uBuffer = uPlane.buffer // Data from U channel
                val vBuffer = vPlane.buffer // Data from V channel
            }
            Log.e("better", "。。。。");
            image?.close()
        }
    }

    // =====================
//    @WorkerThread
    private fun getOptimalSize(cameraCharacteristics: CameraCharacteristics,
                               clazz: Class<*>, maxWidth: Int, maxHeight: Int): Size? {
        val aspectRatio = maxWidth.toFloat() / maxHeight
        val streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = streamConfigurationMap?.getOutputSizes(clazz)
        if (supportedSizes != null) {
            for (size in supportedSizes) {
                if (size.width.toFloat() / size.height == aspectRatio && size.height <= maxHeight && size.width <= maxWidth) {
                    return size
                }
            }
        }
        return supportedSizes?.get(4)
    }

    private fun checkRequiredPermissions(): Boolean {
        val deniedPermissions = mutableListOf<String>()
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission)
            }
        }
        if (deniedPermissions.isEmpty().not()) {
            requestPermissions(deniedPermissions.toTypedArray(), REQUEST_PERMISSION_CODE)
        }
        return deniedPermissions.isEmpty()
    }
}
