package com.github.android.sample.ipc.forecast

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.github.android.sample.ForecaseEntity
import com.github.android.sample.IForecaseAidlInterface
import com.github.android.sample.IForecaseAidlListener

class ForecaseService : Service() {

    // mock data
    val datas = listOf(
            ForecaseEntity("010", "北京", 38, "The Weather is very hot"),
            ForecaseEntity("0731", "长沙", 35, "The Weather is very hot"),
            ForecaseEntity("0732", "湘潭", 35, "The Weather is very hot"),
            ForecaseEntity("0733", "株洲", 35, "The Weather is very hot"),
            ForecaseEntity("020", "广州", 35, "The Weather is very hot"),
            ForecaseEntity("021", "上海", 35, "The Weather is very hot")
    )

    private val binder = object : IForecaseAidlInterface.Stub() {
//        // 运行在 binder 线程中，模拟耗时操作
//        override fun getForecase(cityCode: String?): ForecaseEntity? {
//            // Thread.sleep(3000)
//            e("ThreadName: ${Thread.currentThread().name}")
//            return cityCode?.let { datas.takeWhile { it.cityCode == cityCode }?.get(0) }
//        }

        /**
         * 模拟耗时，通过回调的方式传给客户端
         */
        override fun getForecase(cityCode: String?, listener: IForecaseAidlListener?) {
            // 模拟耗时
            Thread.sleep(3000)
            val entity = cityCode?.let { datas.takeWhile { it.cityCode == cityCode }?.get(0) }
            listener?.let {
                it.onResult(entity)     // 挂起
                println("ok")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = binder
}