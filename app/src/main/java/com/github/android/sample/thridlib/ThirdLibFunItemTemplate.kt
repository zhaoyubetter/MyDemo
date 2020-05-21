package com.github.android.sample.thridlib

import android.app.Activity
import android.content.Context
import com.better.base.model.SampleItem
import com.github.android.sample.concurrent.WorkThreadActivity
import com.github.android.sample.ipc.aidl.BookManager2Activity
import com.github.android.sample.ipc.aidl.BookManager3Activity
import com.github.android.sample.ipc.aidl.BookManagerActivity
import com.github.android.sample.ipc.forecast.ForecaseActivity
import com.github.android.sample.ipc.messenger.Messenger2Activity
import com.github.android.sample.ipc.messenger.MessengerActivity
import com.github.android.sample.ipc.messenger.MessengerPassActivity
import com.github.android.sample.ipc.resultreceiver.ResultReceiverTest1Activity
import com.github.android.sample.ipc.sample.AlbumIPCActivity
import com.github.android.sample.ipc.socket.TcpSocketClientActivity
import com.github.android.sample.ipc.subthread.SubThreadHandleActivity
import com.github.android.sample.solution.JSBrigeActivity
import com.github.android.sample.thridlib.tencent.TencentLbsActivity
import com.github.android.sample.widget.recyler.LayoutManager1Activity
import com.github.android.sample.widget.recyler.RecyclerViewBaseUseActivity
import com.github.android.sample.widget.room.Room1Activity
import com.github.android.sample.widget.viewgroup.FlowLayoutActivity
import com.github.android.sample.widget.viewgroup.WaterFallActivity

/**
 * Created by zhaoyu on 2018/3/11.
 */
class ThirdLibFunItemTemplate private constructor(ctx: Context) {

    // 伴生对象
    companion object {
        private var instance: ThirdLibFunItemTemplate? = null
        private val items = mutableListOf<SampleItem<Activity>>()
        private val groupsItems = mutableMapOf<Int, List<SampleItem<Activity>>>()

        fun getInstance(context: Context): ThirdLibFunItemTemplate {
            if (instance == null)
                instance = ThirdLibFunItemTemplate(context)
            return instance!!
        }

        private fun item(closure: SampleItem<Activity>.() -> Unit) {
            items.add(SampleItem<Activity>().apply(closure))
        }

        // 分组模板
        private fun group(closure: () -> Unit) {
            closure.invoke()
            groupsItems += items.groupBy { it.pid }     // 根据pid进行分组
        }
    }

    operator fun get(id: Int?) = groupsItems[id]
    operator fun contains(id: Int?) = groupsItems.any { it.key == id }

    init {
        group {
            item {
                pid = 0
                id = 1
                title = "腾讯地图与定位"
                desc = "腾讯地图与定位"

                item {
                    pid = 1
                    id = 11
                    title = "腾讯定位服务"
                    desc = "腾讯定位服务"
                    clazz = TencentLbsActivity::class.java
                }
            }

            item {
                pid = 0
                id = 2
                title = "并发"
                desc = "并发问题"

                item {
                    pid = 2
                    id = 21
                    title = "WorkThread"
                    desc = "WorkThread"
                    clazz = WorkThreadActivity::class.java
                }
            }
        }
    }
}