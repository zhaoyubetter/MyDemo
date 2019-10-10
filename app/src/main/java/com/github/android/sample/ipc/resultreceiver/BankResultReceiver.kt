package com.github.android.sample.ipc.resultreceiver

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log

/**
 * 自定义的ResultReceiver
 * handler 用与指定回调执行 （onReceiveResult） 所在线程
 * @author zhaoyu1  2019/5/22
 **/
class BankResultReceiver(handler: Handler) : ResultReceiver(handler) {

    companion object {
        val RESULT_CODE_OK = 1100
        val RESULT_CODE_ERROR = 666
        val PARAM_EXCEPTION = "exception"
        val PARAM_RESULT = "result"
        val PARAM_LIST = "list"
    }

    var callback: ResultReceiverCallback<Any>? = null

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        super.onReceiveResult(resultCode, resultData)

        resultData.classLoader = javaClass.classLoader

        callback?.apply {
            if (resultCode === RESULT_CODE_OK) {
                // 如果 bundle 有非基本数据类型 会报错：classnotfound found； 需要先设置  resultData.classLoader = javaClass.classLoader
                this.onSuccess(resultData.getSerializable(PARAM_RESULT))
            } else {
                this.onError(resultData.getSerializable(PARAM_EXCEPTION) as Exception)
            }
        }
    }

    /**
     * 回调接口，将后端任务的数据传递给调用者
     */
    interface ResultReceiverCallback<T> {
        fun onSuccess(data: T)
        fun onError(e: Exception)
    }

}