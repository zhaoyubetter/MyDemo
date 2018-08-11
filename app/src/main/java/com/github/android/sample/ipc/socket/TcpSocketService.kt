package com.github.android.sample.ipc.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
import com.better.base.d
import com.better.base.e
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.*

/**
 * 有问题的
 */
class TcpSocketService : Service() {

    var isDestroy = false
    val serverMessages = listOf("Hello,Dear", "Good Bye", "Just do it", "Keep moving!")

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Thread {
            Service().run()
        }.start()
    }

    private inner class Service : Runnable {
        override fun run() {
            try {
                val socketService = ServerSocket(8688)
                d("server created ok!")
                while (!isDestroy) {
                    val client = socketService.accept()
                    d("client accept")
                    responseClient(client)
                }
            } catch (e: Exception) {
                e("server created fail: $e")
                return
            }
        }
    }

    private fun responseClient(client: Socket?) {
        client?.let {
            val myIn = BufferedReader(InputStreamReader(client.getInputStream()))
            val myOUt = PrintWriter(BufferedWriter(OutputStreamWriter(client.getOutputStream())))
            myOUt.write("Welcome to Chat!")
            while (!isDestroy) {
//                val clientMsg = myIn.readLine()
//                d("msg from client ==> $clientMsg")
//                if (TextUtils.isEmpty(clientMsg)) break

                val serverMsg = serverMessages[Random().nextInt(serverMessages.size)]
                myOUt.println(serverMsg)
                d("msg from server ==> $serverMsg")
            }

            d("client quit!!")
            myIn.close()
            myOUt.close()
            it.close()
        }
    }

    override fun onDestroy() {
        isDestroy = true
        super.onDestroy()
    }
}
