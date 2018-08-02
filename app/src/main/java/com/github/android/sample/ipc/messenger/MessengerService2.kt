package com.github.android.sample.ipc.messenger

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import com.better.base.e
import org.json.JSONObject


/**
 * Service
 * 排序执行服务
 */
class MessengerService2 : Service() {

    // 将客户端发来的消息交由 MessengerHandler 处理
    private val messenger: Messenger = Messenger(MessengerHandler(this@MessengerService2))

    private class MessengerHandler(val context: Context) : Handler() {
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

            // 获取屏幕密度
                MSG_GET_DENSITY_FROM_CLIENT -> {
                    msg.replyTo?.apply {
                        val respMsg = Message.obtain(null, MSG_RESP_DENSITY).apply {
                            data = Bundle().apply {
                                putString("reply", getDensity().toString())
                            }
                        }
                        send(respMsg) // 响应客户端
                    }
                }

            // 计算
                MSG_REQ_CAL -> {
                    Thread.sleep(1000)      // 模拟耗时
                    msg.replyTo?.apply {
                        val a = msg.data.getInt("a")
                        val b = msg.data.getInt("b")
                        val widgetId = msg.data.getInt("widgetId")
                        val respMsg = Message.obtain(null, MSG_RESP_CAL).apply {
                            data = Bundle().apply {
                                putInt("answer", a + b)
                                putInt("widgetId", widgetId)
                            }
                        }
                        send(respMsg) // 响应客户端
                    }
                }
            }
        }

        private inline fun getDensity(): JSONObject {
            val metrixs = context.resources.displayMetrics
            return JSONObject().apply {
                put("density", metrixs.density)
                put("densityDpi", metrixs.densityDpi)
                put("xDpi", metrixs.xdpi)
                put("yDpi", metrixs.ydpi)
                put("scaledDensity", metrixs.scaledDensity)
                put("width", metrixs.widthPixels)
                put("height", metrixs.heightPixels)
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