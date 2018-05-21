package com.github.android.sample.solution

import android.net.Uri
import android.os.Build
import android.support.annotation.Keep
import android.text.TextUtils
import android.webkit.JsPromptResult
import android.webkit.WebView
import android.webkit.WebChromeClient
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import android.webkit.ValueCallback


// 协议格式如下：
//  me://jsbridge?data={"type":"1","attach":{"msg":"你好，better"}}&callback=304594818

/** 协议中的各个部分 **/
internal val KEY_SCHEME = "me"
internal val KEY_HOST = "jsbridge"
internal val KEY_DATA = "data"
internal val KEY_TYPE = "type"
internal val KEY_ATTACH = "attach"
internal val KEY_CALLBACK = "callback"

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
    }

    fun apply(jsonObject: JSONObject) {
        val execJs = String.format(CALLBACK_JS_FORMAT, port, jsonObject.toString())
        webViewRef?.get()?.apply {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {   // 4.4含以上
                evaluateJavascript(execJs, object : ValueCallback<String> {
                    override fun onReceiveValue(value: String?) {
                        // TODO://
                    }
                })
            } else {
                post{loadUrl(execJs) }
            }
        }
    }
}

@Keep
internal class JSBridge {

    companion object {
        val exposedMethods = HashMap<String, HashMap<String, Method>>()
        /** type 与 原生方法名映射 */
        val type2Method = HashMap<String, String>()

        /**
         * 注册映射方法，一对多
         * @param exposedName 映射大类名称
         * @param jClass   类字节码
         */
        fun register(exposedName: String, jClass: Class<out IBrige>) {
            if (!exposedMethods.containsKey(exposedName)) {
                exposedMethods.put(exposedName, getAllMethod(jClass))
                // TODO : 这里需要修正下，耦合有点高
                type2Method.putAll(BridgeImpl.getType2Method())
            }
        }

        private fun getAllMethod(jClass: Class<out IBrige>): HashMap<String, Method> {
            val methodMap = HashMap<String, Method>()
            jClass.declaredMethods.filter {
                ((it.modifiers == Modifier.PUBLIC or Modifier.STATIC or Modifier.FINAL)
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


        // me://jsbridge?data={"type":"1","attach":{"msg":"你好，better"}}&callback=304594818
        fun callNative(webView: WebView, uriString: String): String {
            lateinit var className: String
            lateinit var callback: String    // 回调地址
            var methodName: String? = null   // 原生方法名
            var param = "{}"                // 方法参数

            if (!TextUtils.isEmpty(uriString) && uriString.startsWith(KEY_SCHEME)) {
                Uri.parse(uriString).apply {
                    if (host == KEY_HOST) {
                        try {
                            val dataJson = JSONObject(getQueryParameter(KEY_DATA))    // 数据
                            val type = dataJson.opt(KEY_TYPE)                       // 类型
                            param = dataJson.optString(KEY_ATTACH)          // 调用参数
                            callback = getQueryParameter(KEY_CALLBACK)      // 回调地址
                            methodName = type2Method.get(type)              // 获取方法名
                        } catch (e: Exception) {

                        }
                    } else {
                        // 其他
                    }
                }
            }

            // 如果找到了方法，则执行该方法
            methodName?.let {
                exposedMethods.forEach { kclazz, map ->
                    map[it]?.let {
                        it.invoke(null, webView, JSONObject(param), Callback(webView, callback))
                        return@forEach
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