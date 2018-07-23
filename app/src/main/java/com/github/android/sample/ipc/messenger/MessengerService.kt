package com.github.android.sample.ipc.messenger

import android.app.Service
import android.content.Intent
import android.os.*
import com.better.base.e


internal const val MSG_FROM_CLIENT = 2
internal const val MSG_FROM_SERVICE = 3

/**
 * Service
 */
class MessengerService : Service() {

    // 将客户端发来的消息交由 MessengerHandler 处理
    private val messenger: Messenger = Messenger(MessengerHandler())

    class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_FROM_CLIENT -> {
                    e("msg from client: ${msg.data}")
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