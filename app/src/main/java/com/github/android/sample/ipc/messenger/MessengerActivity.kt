package com.github.android.sample.ipc.messenger

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.better.base.*
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_messenger.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MessengerActivity : ToolbarActivity() {

    private val TAG = "MessengerActivity"

    private var messenger: Messenger? = null

    // 有内存泄露问题，内部类
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service.isNotNull()) {
                e("onServiceConnected $name")
                messenger = Messenger(service)
            }
        }

        // 调用时机：当service被外界销毁是调用，比如：内存资料不足的时候
        override fun onServiceDisconnected(name: ComponentName?) {
            e("onServiceDisconnected called ----> $name")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

        //  bind service
        btn_bind.setOnClickListener {
            bindService(Intent(this@MessengerActivity, MessengerService::class.java), connection, Context.BIND_AUTO_CREATE)
        }

        // 发送消
        btn_send_msg.setOnClickListener {
            val msg = Message.obtain(null, MSG_FROM_CLIENT).apply {
                data = Bundle().apply {
                    putString("msg", "this message is from better client")
                }
            }
            messenger?.send(msg)    // 发送消息
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(messenger != null) {
            unbindService(connection)
        }
    }
}
