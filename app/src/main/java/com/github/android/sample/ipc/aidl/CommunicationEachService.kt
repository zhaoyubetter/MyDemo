package com.github.android.sample.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.better.base.getProcessName
import com.github.android.sample.ICommuncationEachCallback
import com.github.android.sample.ICommuncationEachInterface
import org.json.JSONObject

/**
 * Created by better On 5/18/21.
 * Remote Service
 */
class CommunicationEachService : Service() {

    val TAG = "CommunicationService"

    private val mCallbacks: RemoteCallbackList<ICommuncationEachCallback> = RemoteCallbackList()
    private val apis: ApiManager = ApiManager()
    private val binder = CommunicationEachStub()

    override fun onBind(intent: Intent?) = binder

    /**
     * 远程进程运行
     */
    private fun async(name: String, params: JSONObject) {
        Log.d(TAG, "async invoke, name: $name, params: $params, process: ${applicationContext.getProcessName()}, thread:${Thread.currentThread().name}")
        val cb = object : Api.IpcCallbackWrapper {
            override fun onIpcInvokedCompleted(response: IpcResponse) {
                val n = mCallbacks.beginBroadcast()
                Log.d(TAG, "async invoked back name: $name, response: ${response.data}, callBackSize: $n 个")

                for (i in 0 until n) {
                    if (mCallbacks.getBroadcastCookie(i) != name) {
                        continue
                    }
                    try {
                        // 最终回到发起进程
                        val apiResult = Bundle().apply {
                            putString("msg", response.msg)
                            putString("data", response.data)
                            putInt("code", response.code)
                        }
                        mCallbacks.getBroadcastItem(i).onComplete(apiResult)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                        Log.e(TAG, "异步调用异常，<--> name: $name, e:$e")
                    }
                }
                mCallbacks.finishBroadcast()
            }
        }

        val realApi = apis.remoteApis[name]
        if (realApi == null) {
            val apiResult = IpcResponse(msg = "fail:not found api: $name", code = -1, data = "")
            cb.onIpcInvokedCompleted(apiResult)
        } else {
            realApi.invoke(name, params, cb)
        }
    }

    private fun sync(name: String, params: JSONObject): Bundle {
        Log.d(TAG, "sync invoke, name: $name, params: $params, process: ${applicationContext.getProcessName()}, thread:${Thread.currentThread().name}")
        val realApi = apis.remoteApis[name]
        var response: IpcResponse? = null;
        response = if (realApi == null) {
            IpcResponse(msg = "fail:not found api: $name", code = -1, data = "")
        } else {
            realApi.invokeSync(name, params)
        }
        return Bundle().apply {
            putString("msg", response?.msg)
            putString("data", response?.data)
            putInt("code", response?.code ?: 0)
        }
    }

    /////////
    inner class CommunicationEachStub : ICommuncationEachInterface.Stub() {
        /**
         * 监听进程死亡
         * TODO
         */
        override fun setDeathRecipient(iBinder: IBinder?, str: String?) {
            /*
            try {
                val dr = IBinder.DeathRecipient() {
                    override fun binderDied() {
                        iBinder?.unlinkToDeath(deathRecipientListeners.get(str), 0);
                        deathRecipientListeners.remove(str);
                        notifyDisconnected(str);
                        Log.e(TAG, String.format("Sub Process Died: %s", str));
                    }
                };
                iBinder?.linkToDeath(dr, 0);
                deathRecipientListeners.put(str, dr);
                notifyConnected(str);
            } catch (e: Exception) {
                Log.e(TAG, "setDeathRecipient: %s", e);
            }
             */
        }

        override fun sendAsync(bundle: Bundle, callback: ICommuncationEachCallback) {
            val eventName = bundle.getString("name") ?: ""
            val params = JSONObject(bundle.getString("params") ?: "")
            mCallbacks.register(callback, eventName)
            async(eventName, params)
        }

        override fun sendSync(bundle: Bundle): Bundle {
            val eventName = bundle.getString("name") ?: ""
            val params = JSONObject(bundle.getString("params") ?: "")
            return sync(eventName, params)
        }
    }
}