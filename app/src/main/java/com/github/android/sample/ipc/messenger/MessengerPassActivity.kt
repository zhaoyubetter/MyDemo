package com.github.android.sample.ipc.messenger

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.better.base.e
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_messenger_pass.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.lang.ref.WeakReference

/**
 * 传递 messenger 给服务端
 */
class MessengerPassActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger_pass)

        val uris = ArrayList<Uri>().apply {
            add(Uri.parse("http://sonar.vip"))
            add(Uri.parse("http://conar.cn"))
            add(Uri.parse("http://bbcc.vip"))
        }

        val messenger = Messenger(ClientHandler(MessengerPassActivity@this))
        e("sender: $messenger")

        btn_start.setOnClickListener {
            val intent = Intent(applicationContext, MessengerPassService::class.java)
            intent.putExtra("client-messenger", messenger)
            intent.putExtra("uris", uris)
            startService(intent)
        }
    }

    class ClientHandler(client: AppCompatActivity) : Handler() {

        val clientRef = WeakReference(client)

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            msg?.let {
                clientRef.get()?.apply {
                    if (msg.what == 0) {
                        val uri = msg.data.getParcelable<Uri>("completed-uri")
                        Log.e("MessengerPass", "$uri")
                    }
                }
            }
        }
    }
}
