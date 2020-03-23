package com.github.android.sample.solution

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.webkit.WebView
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import org.jetbrains.anko.find
import android.webkit.JsPromptResult
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.activity_jsbrige.*
import org.json.JSONObject
import android.os.Environment.getExternalStorageDirectory
import com.better.base.toast
import com.github.android.sample.third.GlideEngine
import com.github.android.sample.tools.PhotoUtil
import com.yu.bundles.album.AlbumListener
import com.yu.bundles.album.MaeAlbum
import org.json.JSONException


class JSBrigeActivity : ToolbarActivity() {

    lateinit var webview: WebView;

    // 模拟低版本 Android 消耗内存，加上后端多开几个进程。webview 直接加载本地图片时闪退。所以webview加载图片需要压缩。
    val obj = Array(1500000) {Integer.MAX_VALUE}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jsbrige)
        setTitleFromIntent(intent)
        webview = find(R.id.webview)


        // 注册一个
        JSBridge.register("bridge", BridgeImpl::class.java)

        webview.apply {
            settings.javaScriptEnabled = true
            webChromeClient = JSBridgeWebChromeClient()
            loadUrl("file:///android_asset/h5/jsBrige.html") //
        }

        // 原生调用js
        btn_call_js.setOnClickListener {
            val json = "{'data':'11233'}"
            JSBridge.callH5(webview, "testH5Func", JSONObject(json))
//            webview.loadUrl("javascript:JSBridge._handleMessageFromNative($json);")
        }

        // 加载本地图片
        btn_load_img.setOnClickListener {
            webview.loadUrl("file:///android_asset/h5/load_local_photo.html")
            toast(getApplicationMemory(this))
        }
    }

    fun getApplicationMemory(context: Context): Long {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                ?: return 0
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem / 1024 / 1024
    }

    private fun showPic(p0: MutableList<String>?) {
        val sb = StringBuilder()
        p0?.forEach {
            val newPath = PhotoUtil.copyImageFile(applicationContext, it)
            sb.append("<br><img src='file://${newPath}' width=\"100\" height=\"100\"><br>")
        }
        val html = "<html><head></head><body>${sb.toString()}</body></html>"
        webview.loadDataWithBaseURL("", html, "text/html", "utf-8", "")
    }
}
