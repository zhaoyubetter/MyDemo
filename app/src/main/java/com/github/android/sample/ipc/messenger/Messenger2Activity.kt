package com.github.android.sample.ipc.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.better.base.ToolbarActivity
import com.better.base.e
import com.better.base.isNotNull
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_messenger2.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class Messenger2Activity : ToolbarActivity() {

    // 2个messenger对象
    private val getReplyMessenger: Messenger = Messenger(MessengerHandler())
    private var messenger: Messenger? = null


    // ==== 接收服务端响应消息
    private class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_FROM_SERVICE -> {
                    e("msg from service: ${msg.data.getString("reply")}")
                }
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service.isNotNull()) {
                messenger = Messenger(service)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger2)
        setTitleFromIntent(intent)

        // bind service
        btn_bind.onClick {
            Intent(this@Messenger2Activity, MessengerService2::class.java).apply {
                bindService(this, connection, Context.BIND_AUTO_CREATE)
            }
        }

        // send msg
        btn_send_msg.onClick {
            val msg = Message.obtain(null, MSG_FROM_CLIENT).apply {
                data = Bundle().apply {
                    putString("msg", "this msg is from client")
                }
                // === 接收服务端回复的messenger
                replyTo = getReplyMessenger
            }
            messenger?.send(msg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }
}
