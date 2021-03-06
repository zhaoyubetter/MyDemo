package com.github.android.sample.problem

import android.app.Activity
import android.content.Context
import com.better.base.model.SampleItem
import com.github.android.sample.problem.memory.MemoryBitmapActivity
import com.github.android.sample.problem.webview.WebViewSwipeConflictActivity

/**
 * Created by zhaoyu on 2018/3/11.
 */
class ProblemFunItemTemplate private constructor(ctx: Context) {

    // 伴生对象
    companion object {
        private var instance: ProblemFunItemTemplate? = null
        private val items = mutableListOf<SampleItem<Activity>>()
        private val groupsItems = mutableMapOf<Int, List<SampleItem<Activity>>>()

        fun getInstance(context: Context): ProblemFunItemTemplate {
            if (instance == null)
                instance = ProblemFunItemTemplate(context)
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
                title = "WebView相关问题"
                desc = "WebView相关问题"

                item {
                    pid = 1
                    id = 11
                    title = "Webview 与原生滑动冲突"
                    desc = "Webview 与原生滑动冲突"
                    clazz = WebViewSwipeConflictActivity::class.java
                }
            }

            item {
                pid = 0
                id = 2
                title = "内存优化相关"
                desc = "内存优化相关"

                item {
                    pid = 2
                    id = 21
                    title = "图片内存优化"
                    desc = "图片内存优化"
                    clazz = MemoryBitmapActivity::class.java
                }
            } // end 内存相关
        }
    }
}