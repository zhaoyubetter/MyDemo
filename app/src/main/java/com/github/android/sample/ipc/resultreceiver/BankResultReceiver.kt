package com.github.android.sample.ipc.resultreceiver

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

/**
 * 自定义的ResultReceiver
 * @author zhaoyu1  2019/5/22
 **/
class BankResultReceiver(handler: Handler) : ResultReceiver(handler) {

    companion object {
        val RESULT_CODE_OK = 1100
        val RESULT_CODE_ERROR = 666
        val PARAM_EXCEPTION = "exception"
        val PARAM_RESULT = "result"
    }

    var callback: ResultReceiverCallback<Any>? = null

    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        super.onReceiveResult(resultCode, resultData)
        callback?.apply {
            if (resultCode === RESULT_CODE_OK) {
                this.onSuccess(resultData.getSerializable(PARAM_RESULT))
            } else {
                this.onError(resultData.getSerializable(PARAM_EXCEPTION) as Exception)
            }
        }
    }

    /**
     * 回调接口
     */
    interface ResultReceiverCallback<T> {
        fun onSuccess(data: T)
        fun onError(e: Exception)
    }

}