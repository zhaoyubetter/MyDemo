package com.github.android.sample.ipc.messenger

import android.app.IntentService
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.os.Messenger
import android.os.SystemClock
import com.better.base.e
import com.better.base.isNotNull

/**
 * IntentService
 * @author zhaoyu1  2019-09-25
 **/
class MessengerPassService(val name: String = "") : IntentService(name) {

    /**
     * Running on sub thread.
     */
    override fun onHandleIntent(intent: Intent) {
        val uris = intent.getParcelableArrayListExtra<Uri>("uris")
        val clientMessenger: Messenger? = intent.getParcelableExtra("client-messenger")
        e("clientMessenger: $clientMessenger")      // 跟客户端的Messenger是同一个对象

        if (clientMessenger.isNotNull()) {
            for (uri in uris) {
                SystemClock.sleep(2000)     // 模拟耗时任务

                val msg = Message.obtain().apply {
                    this.what = 0
                    this.data = Bundle().apply {
                        putParcelable("completed-uri", uri)
                    }
                }

                clientMessenger?.send(msg)
            }
        }
    }
}