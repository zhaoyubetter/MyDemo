package com.github.android.sample.anim

import android.app.Activity
import android.content.Context
import com.better.base.model.SampleItem

/**
 * Created by zhaoyu on 2018/3/11.
 */
class AnimFunTemplate private constructor(ctx: Context) {

    // 伴生对象
    companion object {
        private var instance: AnimFunTemplate? = null
        private val items = mutableListOf<SampleItem<Activity>>()
        private val groupsItems = mutableMapOf<Int, List<SampleItem<Activity>>>()

        fun getInstance(context: Context): AnimFunTemplate {
            if (instance == null)
                instance = AnimFunTemplate(context)
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
            // sampleItem
            item {
                pid = 0
                id = 1
                title = "Android 动画体系"
                desc = "介绍tween动画、属性动画"

                // dsl 嵌套
                item {
                    pid = 1
                    title = "tween动画"
                    desc = "tween动画基本使用"
                    clazz = TweenAnim1Activity::class.java
                }

                item {
                    pid = 1
                    title = "tween动画Interpolator"
                    desc = "tween动画Interpolator详解"
                    clazz = TweenAnimInterpolatorActivity::class.java
                }
            }
        }
    }
}