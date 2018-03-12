package com.better.base.model

/**
 * cz
 */
data class SampleItem<T>(var id: Int?, var pid: Int = 0,
                         var clazz: Class<out T>?, var title: String?, var desc: String?) {
    constructor() : this(null, 0, null, null, null)
}


