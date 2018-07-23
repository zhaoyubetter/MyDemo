package com.github.android.sample.ipc.messenger

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import com.better.base.*
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_messenger.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MessengerActivity : ToolbarActivity() {

    private val TAG = "MessengerActivity"

    private var messenger: Messenger? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service.isNotNull()) {
                e("onServiceConnected $name")
                messenger = Messenger(service)
            }
        }

        // 当service被外界销毁是调用，比如：内存资料不足的时候
        override fun onServiceDisconnected(name: ComponentName?) {
            e("onServiceDisconnected called ----> $name")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)
        setTitleFromIntent(intent)

        //  bind service
        btn_bind.onClick {
            Intent(this@MessengerActivity, MessengerService::class.java).apply {
                bindService(this, connection, Context.BIND_AUTO_CREATE)
            }
        }

        // 发送消
        btn_send_msg.onClick {
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
        unbindService(connection)
    }
}