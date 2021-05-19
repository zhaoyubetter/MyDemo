package com.github.android.sample.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import com.better.base.getProcessName
import com.github.android.sample.ICommuncationEachCallback
import com.github.android.sample.ICommuncationEachClientInterface
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
    private val clients = HashMap<String, ICommuncationEachClientInterface>()

    override fun onBind(intent: Intent?) = binder

    /**
     * 远程进程运行
     */
    private fun async(name: String, params: JSONObject, bundle: Bundle) {
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
        if ("sendEvent" == name) {
            // 服务端发送消息给客户端
            sendEventToClient(bundle)
        } else if(realApi == null)  {
            val apiResult = IpcResponse(msg = "fail:not found api: $name", code = -1, data = "")
            cb.onIpcInvokedCompleted(apiResult)
        } else {
            realApi.invoke(name, params, cb)
            // 直接通过 bundle 将返回值写回给调用方
            bundle.putString("serverInfo", "来着服务端添加的信息")
        }
    }

    private fun sendEventToClient(bundle:Bundle) {
        val appIds = bundle.getStringArrayList("appIds")
        val data = bundle.getString("data")
        Log.d(TAG, "收到服务发送事件请求, name: sendEvent, data: $data, process: ${applicationContext.getProcessName()}, thread:${Thread.currentThread().name}")
        val resp = Bundle()
        resp.putString("name", "event_play")
        resp.putString("data", """ 
                 {
                    "videoId":1,
                    "url":"http://111.com"
                 }
            """.trimIndent())
        if(appIds == null || appIds.isEmpty()) {

            clients.forEach { (t, u) -> u.sendEventToMp(resp)}
        } else {
            for (id in appIds) {
                clients.get(id)?.sendEventToMp(resp)
            }
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
            val params = JSONObject(bundle.getString("data") ?: "")
            mCallbacks.register(callback, eventName)
            async(eventName, params, bundle)
        }

        override fun sendSync(bundle: Bundle): Bundle {
            val eventName = bundle.getString("name") ?: ""
            val params = JSONObject(bundle.getString("data") ?: "")
            return sync(eventName, params)
        }

        override fun registerClient(appId: String, client: ICommuncationEachClientInterface) {
            synchronized(this) {
                clients.put(appId, client)
                Log.d(TAG, "registerClient, appId: $appId, clientSize: ${clients.size}")
            }
        }

        override fun unRegisterClient(appId: String) {
            synchronized(this) {
                clients.remove(appId)
                Log.d(TAG, "unRegisterClient, appId: $appId, clientSize: ${clients.size}")
            }
        }
    }
}