package com.github.android.sample.widget

import android.app.Activity
import android.content.Context
import com.better.base.model.SampleItem
import com.github.android.sample.BaseFunItemTemplate
import com.github.android.sample.anim.*
import com.github.android.sample.canvas_paint.*
import com.github.android.sample.ipc.aidl.BookManager2Activity
import com.github.android.sample.ipc.aidl.BookManager3Activity
import com.github.android.sample.ipc.aidl.BookManagerActivity
import com.github.android.sample.ipc.messenger.Messenger2Activity
import com.github.android.sample.ipc.messenger.MessengerActivity
import com.github.android.sample.solution.JSBrigeActivity
import com.github.android.sample.widget.recyler.LayoutManager1Activity
import com.github.android.sample.widget.recyler.RecyclerViewBaseUseActivity
import com.github.android.sample.widget.room.Room1Activity
import com.github.android.sample.widget.viewgroup.FlowLayoutActivity
import com.github.android.sample.widget.viewgroup.WaterFallActivity

/**
 * Created by zhaoyu on 2018/3/11.
 */
class WidgetFunItemTemplate private constructor(ctx: Context) {

    // 伴生对象
    companion object {
        private var instance: WidgetFunItemTemplate? = null
        private val items = mutableListOf<SampleItem<Activity>>()
        private val groupsItems = mutableMapOf<Int, List<SampleItem<Activity>>>()

        fun getInstance(context: Context): WidgetFunItemTemplate {
            if (instance == null)
                instance = WidgetFunItemTemplate(context)
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
                title = "自定义控件-ViewGroup"
                desc = "ViewGroup自定义控件"

                item {
                    pid = 1
                    id = 11
                    title = "FlowLayout - 流布局"
                    desc = "FlowLayout"
                    clazz = FlowLayoutActivity::class.java
                }

                item {
                    pid = 1
                    id = 12
                    title = "WaterFallLayout - 瀑布流"
                    desc = "WaterFallLayout-简单示例"
                    clazz = WaterFallActivity::class.java
                }
            }

            // recyclerView
            item {
                pid = 0
                id = 2
                title = "RecyclerView"
                desc = "RecyclerView"

                item {
                    pid = 2
                    id = 20
                    title = "RecyclerView 基本使用"
                    desc = "RecyclerView 基本使用例子"

                    item {
                        pid = 20
                        id = 30
                        title = "RecyclerView 基本使用"
                        desc = "RecyclerView 基本使用"
                        clazz = RecyclerViewBaseUseActivity::class.java
                    }
                }

                item {
                    pid = 2
                    id = 22
                    title = "RecyclerView LayoutManager"
                    desc = "RecyclerView - LayoutManager"

                    item {
                        pid = 22
                        id = 30
                        title = "LayoutManager - 流式布局"
                        desc = "LayoutManager - 流式布局"
                        clazz = LayoutManager1Activity::class.java
                    }
                }
            } // id 2

            item {
                pid = 0
                id = 3
                title = "IPC机制"
                desc = "IPC机制"

                item {
                    pid = 3
                    title = "Messenger消息"
                    desc = "基于消息的进程间通信 - 发送消息"
                    clazz = MessengerActivity::class.java
                }

                item {
                    pid = 3
                    title = "Messenger消息"
                    desc = "基于消息的进程间通信 - 相互通信"
                    clazz = Messenger2Activity::class.java
                }

                item {
                    pid = 3
                    title = "AIDL-简单入门"
                    desc = "AIDL-简单入门"
                    clazz = BookManagerActivity::class.java
                }
                item {
                    pid = 3
                    title = "AIDL-监听"
                    desc = "AIDL-监听"
                    clazz = BookManager2Activity::class.java
                }

                item {
                    pid = 3
                    title = "AIDL-删除监听"
                    desc = "AIDL-删除监听-RemoteCallbackList"
                    clazz = BookManager3Activity::class.java
                }
            } // id 3

            item {
                pid = 0
                id = 4
                title = "Room数据库操作"

                item {
                    pid = 4
                    title = "Room的基本使用"
                    desc = "Room的基本使用"
                    clazz = Room1Activity::class.java
                }
            } // id 4

            item {
                pid = 0
                id = 4
                title = "JSBrige"
                desc = "native h5 双向通信方案"
                clazz = JSBrigeActivity::class.java
            }

        }
    }
}