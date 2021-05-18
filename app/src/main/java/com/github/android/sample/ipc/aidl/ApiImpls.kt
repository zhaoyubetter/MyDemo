package com.github.android.sample.ipc.aidl

import android.os.SystemClock
import android.text.TextUtils
import org.json.JSONObject

/**
 * Created by better On 5/18/21.
 * 获取设备信息 api
 */
class DeviceInfoModule : Api {

    override fun invoke(name: String, params: JSONObject, callback: Api.IpcCallbackWrapper) {
        val data = JSONObject()
        val result = IpcResponse()
        when (name) {
            "getDeviceInfo" -> {
                data.apply {
                    put("os", "android");
                    put("host", "MyDemo")
                    put("hostVersion", "1.0.0")
                    put("frameVersion", "3.6.0")
                }
                result.data = data.toString();
                result.code = 0
                // 模拟耗时
                SystemClock.sleep(3000)
                callback.onIpcInvokedCompleted(result)
            }
            "getDeviceStorageInfo" -> {
                data.apply {
                    put("hasSdcard", "yes")
                    put("totalMemory", "12G")
                    put("availableMemory", "4G")
                    put("remainSize", "64GB")
                }
                result.data = data.toString();
                result.code = 0
                callback.onIpcInvokedCompleted(result)
            }

            else -> {
                result.code = -1;
                callback.onIpcInvokedCompleted(result)
            }
        }
    }

    override fun invokeSync(name: String, params: JSONObject): IpcResponse {
        val data = JSONObject()
        val result = IpcResponse()
        when (name) {
            "getDeviceInfo" -> {
                data.apply {
                    put("os", "android");
                    put("host", "MyDemo")
                    put("hostVersion", "1.0.0")
                    put("frameVersion", "3.6.0")
                }
                result.data = data.toString();
                result.code = 0
                // 模拟耗时
                SystemClock.sleep(3000)
            }
            "getDeviceStorageInfo" -> {
                data.apply {
                    put("hasSdcard", "yes")
                    put("totalMemory", "12G")
                    put("availableMemory", "4G")
                    put("remainSize", "64GB")
                }
                result.data = data.toString();
                result.code = 0
            }

            else -> {
                result.code = -1;
            }
        }
        return result
    }

    override fun getApis(): Array<String> {
        return arrayOf("getDeviceInfo")
    }

    override fun onCreate() {
    }
}

/**
 * 初始化入口
 */
class ApiManager {

    val remoteApis = HashMap<String, Api>()

    init {
        add(DeviceInfoModule())
    }

    private fun add(api: Api) {
        if (api.getApis() != null) {
            val apiNames: Array<String> = api.getApis()
            for (name in apiNames) {
                if (!TextUtils.isEmpty(name)) {
                    remoteApis[name] = api
                }
            }
        }
    }
}