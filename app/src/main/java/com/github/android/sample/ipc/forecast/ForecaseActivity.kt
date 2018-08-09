package com.github.android.sample.ipc.forecast

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.better.base.ToolbarActivity
import com.better.base.e
import com.better.base.setTitleFromIntent
import com.better.base.toast
import com.github.android.sample.ForecaseEntity
import com.github.android.sample.IForecaseAidlInterface
import com.github.android.sample.IForecaseAidlListener
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_forecase.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * 1.虽然服务器是运行在binder线程中，但客户端会挂起，等待响应
 *   所以要避免在UI线程中，调用远程方法；
 *
 * 2.服务端要调用客户端回调接口得方法，被调用的方法也是运行在客户端的binder线程中，调用时服务端挂起，等待客户端返回，所以
 *   在服务端调用客户端也需要注意方法是否耗时，否则导致服务端 anr；
 */
class ForecaseActivity : ToolbarActivity() {

    var isConnection = false
    var foreInterface: IForecaseAidlInterface? = null

    val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            isConnection = true
            foreInterface = IForecaseAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnection = false
        }
    }

    // 回调接口
    val forecastListener = object : IForecaseAidlListener.Stub() {
        override fun onResult(entity: ForecaseEntity?) {
            e("${Thread.currentThread().name}")
            Thread.sleep(3000)      // server 调用 client 模拟耗时
            runOnUiThread {
                toast("$entity, ${Thread.currentThread().name}")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecase)
        setTitleFromIntent(intent)

        // auto bind service
        bindService(Intent("com.github.android.sample.ipc.forecast.ForecaseService")
                .setPackage(this@ForecaseActivity.packageName), serviceConn, Context.BIND_AUTO_CREATE)

        // search
        btn_search.onClick {
            // 虽然服务器是运行在binder线程中，但客户端会挂起，等待响应，所以通过子线程来
            Thread {
                et_cityCode.text?.apply {
                    foreInterface?.getForecase(toString(), forecastListener)
                }
            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isConnection) {
            unbindService(serviceConn)
        }
    }
}
