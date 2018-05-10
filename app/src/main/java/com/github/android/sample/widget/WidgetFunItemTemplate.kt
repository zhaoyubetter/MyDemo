package com.github.android.sample.widget

import android.app.Activity
import android.content.Context
import com.better.base.model.SampleItem
import com.github.android.sample.BaseFunItemTemplate
import com.github.android.sample.anim.*
import com.github.android.sample.canvas_paint.*
import com.github.android.sample.solution.JSBrigeActivity
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
            // 第一部分 动画
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

            item {
                pid = 0
                id = 2
                title = "JSBrige"
                desc = "native h5 双向通信方案"
                clazz = JSBrigeActivity::class.java
            }
        }
    }
}