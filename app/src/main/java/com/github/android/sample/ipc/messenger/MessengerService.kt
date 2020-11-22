package com.github.android.sample.ipc.messenger

import android.app.Service
import android.content.Intent
import android.os.*
import com.better.base.e


internal const val MSG_FROM_CLIENT = 2
internal const val MSG_FROM_SERVICE = 3
internal const val MSG_GET_DENSITY_FROM_CLIENT = 4
internal const val MSG_RESP_DENSITY = 5
internal const val MSG_REQ_CAL = 6
internal const val MSG_RESP_CAL = 7

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
                    // 如果调用方与service非同一进程（service配置了 process），调入方不会挂着，但服务端会挂起来；
                    // 否则，调用方将会挂起，知道任务完成，
                    SystemClock.sleep(3500)
                    e("better", "msg from client: ${msg.data}")
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