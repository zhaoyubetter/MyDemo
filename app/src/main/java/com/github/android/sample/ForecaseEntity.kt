package com.github.android.sample

import android.os.Parcel
import android.os.Parcelable

data class ForecaseEntity(var cityCode: String = "",
                          var cityName: String = "",
                          var temperature: Int = 0,
                          var info: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cityCode)
        parcel.writeString(cityName)
        parcel.writeInt(temperature)
        parcel.writeString(info)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForecaseEntity> {
        override fun createFromParcel(parcel: Parcel): ForecaseEntity {
            return ForecaseEntity(parcel)
        }

        override fun newArray(size: Int): Array<ForecaseEntity?> {
            return arrayOfNulls(size)
        }
    }
}