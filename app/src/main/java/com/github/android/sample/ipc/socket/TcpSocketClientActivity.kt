package com.github.android.sample.ipc.socket

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.better.base.d
import com.better.base.e
import com.better.base.getProcessName
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_tcp_socket_client.*
import java.io.*
import java.net.Socket

class TcpSocketClientActivity : AppCompatActivity() {

    var socket: Socket? = null
    var myOUt: PrintWriter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tcp_socket_client)

        // start service
        startService(Intent(this, TcpSocketService::class.java))

        // try to connect server
        btn_connect.setOnClickListener {
            connectServer()
        }

        // btn_send
        btn_send.setOnClickListener {
            myOUt?.write("Hi, I am a client, name is ${getProcessName()}")
        }
    }

    private fun connectServer() {
        Thread {
            while (socket == null) {
                try {
                    socket = Socket("localhost", 8688)
                    myOUt = PrintWriter(BufferedWriter(OutputStreamWriter(socket?.getOutputStream())))
                    d("client connect server success.")
                    btn_send.post { btn_send.isEnabled = true }
                } catch (e: Exception) {
                    SystemClock.sleep(2000)
                    btn_send.post { btn_send.isEnabled = false }
                    e("client connect server fail. ${e.toString()} retry...")
                }
            }

            try {
                // receiver msg from server
                val myIn = BufferedReader(InputStreamReader(socket?.getInputStream()))
                while (!isFinishing && socket?.isClosed != true) {
                    val serverMsg = myIn.readLine()
                    d("receive msg from server: $serverMsg")
                }
                myIn.close()
                myOUt?.close()
                socket?.close()
            } catch (e: Exception) {
                e(e.toString())
            }
        }.start()
    }

    override fun onDestroy() {
        socket?.let {
            if (socket?.isClosed != true) {
                it.shutdownInput()
                it.close()
            }
        }
        super.onDestroy()
    }
}
