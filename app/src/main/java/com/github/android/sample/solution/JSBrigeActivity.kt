package com.github.android.sample.solution

import android.os.Bundle
import android.webkit.WebView
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import org.jetbrains.anko.find
import android.webkit.JsPromptResult
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.activity_jsbrige.*
import org.json.JSONObject


class JSBrigeActivity : ToolbarActivity() {

    lateinit var webview: WebView;

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
    }
}
