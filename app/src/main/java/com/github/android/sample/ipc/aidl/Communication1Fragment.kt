package com.github.android.sample.ipc.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.better.base.getProcessName
import com.github.android.sample.ICommuncationEachCallback
import com.github.android.sample.ICommuncationEachClientInterface
import com.github.android.sample.ICommuncationEachInterface
import com.github.android.sample.R
import kotlinx.android.synthetic.main.fragment_communication1.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.util.ArrayList

/**
 * 哪个线程发起远程调用，就会回调到哪个线程
 */
class Communication1Fragment : Fragment(), View.OnClickListener {

    val TAG = "Client1"

    var ipcInterface: ICommuncationEachInterface? = null
    var serviceConnection: ServiceConnection? = null
    val mainHandler = Handler(Looper.getMainLooper())
    // Running in Client
    var clientBinder : ClientCommunicationBinder?= null

    lateinit var asyncHandler: Handler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_communication1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val handlerThread = HandlerThread("asyncHandler")
        handlerThread.start()
        asyncHandler = Handler(handlerThread.looper)
        btn_bindService.setOnClickListener(this)
        btn_unBindService.setOnClickListener(this)
        btn_sendToClient.setOnClickListener(this)
        btn_sendToServer.setOnClickListener(this)
        btn_sendToSyncServer.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        asyncHandler.looper.quit()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_bindService -> {
                tryBindService()
            }
            R.id.btn_unBindService -> {
                serviceConnection?.let {
                    tv_status.text = "断开连接..."
                    context?.unbindService(it)
                    serviceConnection = null
                    ipcInterface = null
                }
            }
            R.id.btn_sendToServer -> {
                sendToServe()
            }
            R.id.btn_sendToSyncServer -> {
                sendToServeSync()
            }
            R.id.btn_sendToClient -> {
                serveToClient()
            }
        }
    }

    private fun serveToClient() {
        val name = et_toClient_name.text.toString()
        val params = et_toClient_params.text.toString()
        if (ipcInterface != null && name.isNotBlank()) {
            val bundle = Bundle(2)
            bundle.putString("name", "sendEvent")
            bundle.putString("data", params)
            bundle.putStringArrayList("appIds", ArrayList<String>().apply {
                add("188")
            } )
            asyncHandler.post {
                ipcInterface?.sendAsync(bundle, ipcInvokeCallback)
            }
        }
    }

    //// 回调
    private val apiCallback = object : IApiCallback {
        override fun onSuccess(data: JSONObject) {
            Log.d(TAG, "async callback, process:${context?.getProcessName()}")
            tv_logs.append("成功: $data\n")
            scrollView.scrollTo(0, tv_logs.bottom)
        }

        override fun onFail(error: JSONObject?) {
            tv_logs.append("失败: $error\n")
            scrollView.scrollTo(0, tv_logs.bottom)
        }

        override fun onCancel() {
            tv_logs.append("调用取消: ")
            scrollView.scrollTo(0, tv_logs.bottom)
        }
    }

    //////
    private val ipcInvokeCallback = object : ICommuncationEachCallback.Stub() {
        // 运行在发起方进程
        override fun onComplete(bundle: Bundle) {
            Log.d(TAG, "client, callback back thread: ${Thread.currentThread().name}")
            mainHandler.post {
                val code = bundle.getInt("code")
                val data = bundle.getString("data")
                val msg = bundle.getString("msg")
                when (code) {
                    0 -> apiCallback.onSuccess(JSONObject(data))
                    -1 -> apiCallback.onFail(JSONObject())
                    1 -> apiCallback.onCancel()
                }
            }
        }
    }

    private fun sendToServe() {
        val name = et_toServer_name.text.toString()
        val params = et_toServer_params.text.toString()
        if (ipcInterface != null && name.isNotBlank()) {
            val bundle = Bundle(2)
            bundle.putString("name", name)
            bundle.putString("data", params)
            asyncHandler.post {
                ipcInterface?.sendAsync(bundle, ipcInvokeCallback)
                toast(bundle.getString("serverInfo") ?: "无数据")
            }
            // 测试直接主线程发起
//            ipcInterface?.sendAsync(bundle, ipcInvokeCallback)
        } else {
            toast("IPC 连接未建立")
        }
    }

    private fun sendToServeSync() {
        val name = et_toServer_name.text.toString()
        val params = et_toServer_params.text.toString()
        if (ipcInterface != null && name.isNotBlank()) {
            val paramBundle = Bundle(2)
            paramBundle.putString("name", name)
            paramBundle.putString("data", params)

            // 同步调用
            val bundle = ipcInterface?.sendSync(paramBundle)
            val code = bundle?.getInt("code")
            val data = bundle?.getString("data")
            val msg = bundle?.getString("msg")

            when (code) {
                0 -> apiCallback.onSuccess(JSONObject(data))
                -1 -> apiCallback.onFail(JSONObject())
                1 -> apiCallback.onCancel()
            }
        } else {
            toast("IPC 连接未建立")
        }
    }

    private fun tryBindService() {
        if (serviceConnection == null) {
            tv_status.text = "连接中..."
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    tv_status.text = "已连接..."
                    ipcInterface = ICommuncationEachInterface.Stub.asInterface(service);
                    try {
                        // 这里怎么回事 new Binder 呢？ for Better， 监听死亡
                        ipcInterface?.setDeathRecipient(Binder(), context?.getProcessName());
                    } catch (e: Exception) {
                        Log.e(TAG, "service dead, try to bindService...")
                    }
                    // 创建 stud
                    createClientStubAndRegisterToServe()
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    tv_status.text = "服务断开..."
                    ipcInterface = null
                    tryBindService()
                    Log.e(TAG, "service dead, try to bindService...")
                }
            }
        }
        val i = Intent(activity, CommunicationEachService::class.java);
        context?.bindService(i, serviceConnection!!, Context.BIND_AUTO_CREATE)
    }

    private fun createClientStubAndRegisterToServe() {
        clientBinder = ClientCommunicationBinder()
        ipcInterface?.registerClient("188", clientBinder)
    }

    //// client binder
    inner class ClientCommunicationBinder : ICommuncationEachClientInterface.Stub() {
        override fun sendEventToMp(requestBundle: Bundle): Bundle {
            val respBundle = Bundle()
            val name = requestBundle.getString("name")
            val data = JSONObject(requestBundle.getString("data") ?: "")
            // 处理来自的事件
            tv_logs.post {
                when(name) {
                    "event_play" -> {
                        tv_logs.append("收到播放事件: $data\n")
                        scrollView.scrollTo(0, tv_logs.bottom)
                    }
                    "event_stop" -> {
                        tv_logs.append("收到暂停事件: $data\n")
                        scrollView.scrollTo(0, tv_logs.bottom)
                    }
                    "event_resume" -> {
                        tv_logs.append("收到 resume 事件: $data\n")
                        scrollView.scrollTo(0, tv_logs.bottom)
                    }
                }
            }

            respBundle.putInt("code", 0)
            respBundle.putString("data", "")
            return respBundle
        }
    }

}