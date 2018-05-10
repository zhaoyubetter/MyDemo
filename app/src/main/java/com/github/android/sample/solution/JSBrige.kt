package com.github.android.sample.solution

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.support.annotation.Keep
import android.text.TextUtils
import android.webkit.JsPromptResult
import android.webkit.WebView
import android.webkit.WebChromeClient
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.lang.reflect.Method
import java.lang.reflect.Modifier

@Keep
interface IBrige

@Keep
internal class JSBridgeWebChromeClient : WebChromeClient() {
    override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
        result.confirm(JSBridge.callNative(view, message))
        return true
    }
}

@Keep
class Callback(webView: WebView, private val port: String) {

    private val webViewRef: WeakReference<WebView> = WeakReference(webView)

    companion object {
        val CALLBACK_JS_FORMAT = "javascript:JSBridge.onFinish('%s', %s);"
        val handler = Handler(Looper.getMainLooper())
    }

    fun apply(jsonObject: JSONObject) {
        val execJs = String.format(CALLBACK_JS_FORMAT, port, jsonObject.toString())
        webViewRef?.get()?.let {
            handler.post { it.loadUrl(execJs) }
        }
    }
}

@Keep
internal class JSBridge {

    companion object {
        val exposedMethods = HashMap<String, HashMap<String, Method>>()

        fun register(exposedName: String, jClass: Class<out IBrige>) {
            if (!exposedMethods.containsKey(exposedName)) {
                exposedMethods.put(exposedName, getAllMethod(jClass))
            }
        }

        private fun getAllMethod(jClass: Class<out IBrige>): HashMap<String, Method> {
            val methodMap = HashMap<String, Method>()
            jClass.declaredMethods.filter {
                ((it.modifiers == Modifier.PUBLIC or Modifier.STATIC)
                        || it.name == null) &&
                        (it.parameterTypes?.size == 3 &&
                                it.parameterTypes[0] == WebView::class.java &&
                                it.parameterTypes[1] == JSONObject::class.java &&
                                it.parameterTypes[2] == Callback::class.java)

            }.forEach {
                methodMap[it.name] = it
            }
            return methodMap
        }


        fun callNative(webView: WebView, uriString: String): String {
            lateinit var methodName: String
            lateinit var className: String
            var param = "{}"
            lateinit var port: String

            if (!TextUtils.isEmpty(uriString) && uriString.startsWith("JSBridge")) {
                Uri.parse(uriString).apply {
                    className = host
                    param = query
                    port = "" + getPort()
                    val path = path
                    if (!TextUtils.isEmpty(path)) {
                        methodName = path.replace("/", "")
                    }
                }
            }

            if (exposedMethods.containsKey(className)) {
                val methodHashMap = exposedMethods[className]
                if (methodHashMap != null && methodHashMap.size != 0 && methodHashMap.containsKey(methodName)) {
                    methodHashMap[methodName]?.let {
                        it.invoke(null, webView, JSONObject(param), Callback(webView, port))
                    }
                }
            }

            return ""
        }
    }
}

/*对外的方法封装
Kotlin反射获取不到 jClass.declaredMethods 这里的静态方法，所以这块用Java来弄的
class BridgeImpl2 : IBrige {

    companion object {

        fun showToast(webView: WebView, param: JSONObject, callback: Callback?) {
            val message = param.optString("msg")
            Toast.makeText(webView.context, message, Toast.LENGTH_SHORT).show()
            callback?.let {
                // content内容
                val json = JSONObject()
                json.put("name", "better")
                json.put("age", "30")
                callback.apply(getJSONObject(0, "ok", json))
            }
        }

        private fun getJSONObject(code: Int, msg: String, result: JSONObject): JSONObject {
            val json = JSONObject()
            json.put("code", code)
            json.put("msg", msg)
            json.putOpt("content", result)
            return json
        }
    }
}*/