package com.better.base.model

import android.app.Activity
import android.os.Parcel
import android.os.Parcelable

/**
 * cz
 */
//import kotlinx.android.parcel.Parcelize

//@Parcelize
data class SampleItem<T>(var id: Int?, var pid: Int = 0,
                         var clazz: Class<out T>?, var title: String?, var desc: String?) : Parcelable {
    constructor(parcel: Parcel) : this (
            null, 0, null, null, null) {
    }

    constructor() : this(null, 0, null, null, null)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeInt(pid)
        parcel.writeString(title)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SampleItem<*>> {
        override fun createFromParcel(parcel: Parcel): SampleItem<*> {
            return SampleItem<Activity>(parcel)
        }

        override fun newArray(size: Int): Array<SampleItem<*>?> {
            return arrayOfNulls(size)
        }
    }
}


