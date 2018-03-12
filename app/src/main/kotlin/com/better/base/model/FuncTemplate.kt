package com.better.base.model;

import android.app.Activity
import android.content.Context

/**
 * cz
 */
class FuncTemplate private constructor(ctx: Context) {

    // 伴生对象
    companion object {
        private var instance: FuncTemplate? = null
        private val items = mutableListOf<SampleItem<Activity>>()
        private val groupsItems = mutableMapOf<Int, List<SampleItem<Activity>>>()

        fun getInstance(context: Context): FuncTemplate {
            if (instance == null)
                instance = FuncTemplate(context)
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

    }
}