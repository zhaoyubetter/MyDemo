package com.github.android.sample.ipc.aidl

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import org.json.JSONObject


/**
 * Created by better On 5/18/21.
 */

const val CODE_SUCCESS = 0
const val CODE_FAIL = -1
const val CODE_CANCEL = 1

interface IApiCallback {
    /**
     * Api调用成功
     *
     * @param data Api调用返回的结果
     */
    fun onSuccess(data: JSONObject)

    /**
     * Api调用失败
     */
    fun onFail(error: JSONObject?)

    /**
     * Api调用取消
     */
    fun onCancel()
}


interface Api {
    /**
     * 接收到对应的api调用时，会调用此方法，在此方法中处理api调用的功能逻辑
     *
     * @param name    接口名
     * @param params 参数
     */
    fun invoke(name: String, params: JSONObject, @NonNull callback: IpcCallbackWrapper)

    /**
     * 同步调用
     * @param name    接口名
     * @param params 参数
     */
    fun invokeSync(name: String, params: JSONObject): IpcResponse?

    /**
     * api 集合
     */
    fun getApis(): Array<String>

    fun onCreate()

    /**
     * 为了简化远程api实现代码 用该接口包装一层回调
     * 在回调方法中进行数据跨进程传输
     */
    interface IpcCallbackWrapper {
        fun onIpcInvokedCompleted(response: IpcResponse)
    }
}

/**
 * for 返回值
 */
class IpcResponse(
        var data: String? = "",
        var code: Int = 0,
        var msg: String? = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(data)
        parcel.writeInt(code)
        parcel.writeString(msg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IpcResponse> {
        override fun createFromParcel(parcel: Parcel): IpcResponse {
            return IpcResponse(parcel)
        }

        override fun newArray(size: Int): Array<IpcResponse?> {
            return arrayOfNulls(size)
        }
    }
}

