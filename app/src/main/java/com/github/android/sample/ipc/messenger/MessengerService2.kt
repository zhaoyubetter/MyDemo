package com.github.android.sample.ipc.messenger

import android.app.Service
import android.content.Intent
import android.os.*
import com.better.base.e



/**
 * Service
 */
class MessengerService2 : Service() {

    // 将客户端发来的消息交由 MessengerHandler 处理
    private val messenger: Messenger = Messenger(MessengerHandler())

    private class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_FROM_CLIENT -> {
                    e("msg from client: ${msg.data}")

                    // ====  响应客户端
                    msg.replyTo?.apply {
                        val msg = Message.obtain(null, MSG_FROM_SERVICE).apply {
                            data = Bundle().apply {
                                putString("reply", "收到消息，马上回应你！！！！")
                            }
                        }
                        send(msg)      // 响应客户端
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        e("MessengerService onCreated")
    }

    override fun onBind(intent: Intent?): IBinder {
        e("MessengerService onBind")
        return messenger.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        e("MessengerService onDestroy")
    }
}