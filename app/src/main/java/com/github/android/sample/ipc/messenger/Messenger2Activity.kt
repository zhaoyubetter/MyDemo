package com.github.android.sample.ipc.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import com.github.android.sample.R
import android.os.*
import android.widget.TextView
import com.better.base.*
import kotlinx.android.synthetic.main.activity_keyframe.view.*
import kotlinx.android.synthetic.main.activity_messenger2.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textView
import java.util.*

class Messenger2Activity : ToolbarActivity() {

    // 2个messenger对象
    private val getReplyMessenger: Messenger = Messenger(MessengerHandler())
    private var messenger: Messenger? = null
    private var isConnected:Boolean = false


    // ==== 接收服务端响应消息
    private inner class MessengerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_FROM_SERVICE -> {
                    e("msg from service: ${msg.data.getString("reply")}")
                }
                MSG_RESP_DENSITY -> {
                    e("msg from service: ${msg.data.getString("reply")}")
                    if(!this@Messenger2Activity.isFinishing) {
                        result.text = msg.data.getString("reply")
                    }
                }
                MSG_RESP_CAL-> {
                    if(this@Messenger2Activity.isValid()) {
                        val widgetId = msg.data.getInt("widgetId")
                        val answer = msg.data?.getInt("answer")
                        container_sum.find<TextView>(widgetId)?.apply {
                            text = "$text $answer"
                        }
                    }
                }
            }
        }
    }

    // === 链接对象
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service.isNotNull()) {
                messenger = Messenger(service)
                isConnected = true
            }
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            messenger = null
            isConnected = false
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
                // === 如果需要回信，接收服务端回复的messenger
                replyTo = getReplyMessenger
            }
            messenger?.send(msg) ?: toast("请先绑定远程服务")
        }

        // 获取设备密度
        btn_density.onClick {
            val msg = Message.obtain(null, MSG_GET_DENSITY_FROM_CLIENT).apply {
                replyTo = getReplyMessenger
            }
            messenger?.send(msg)
        }



        // btn_cal
        var widgetId = 1
        btn_cal.onClick {
            if(isConnected) {
                val a = 88
                val b = Random().nextInt(100)
                widgetId++
                with(container_sum) {
                    textView {
                        text = "$a + $b = "
                        id = widgetId
                    }
                }
                val msg = Message.obtain(null, MSG_REQ_CAL).apply {
                    data = Bundle().apply {
                        putInt("a", a)
                        putInt("b", b)
                        putInt("widgetId", widgetId)
                    }
                    replyTo = getReplyMessenger
                }
                messenger?.send(msg)
            }
        }

        btn_test.onClick {
            // 在handle中执行
            getReplyMessenger.send(Message.obtain().apply { what = 100 })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isConnected) {
            unbindService(connection)
        }
    }
}
