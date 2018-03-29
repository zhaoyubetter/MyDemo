package com.better.base.model

import android.os.Parcelable

/**
 * cz
 */
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SampleItem<T>(var id: Int?, var pid: Int = 0,
                         var clazz: Class<out T>?, var title: String?, var desc: String?) : Parcelable {
    constructor() : this(null, 0, null, null, null)
}


