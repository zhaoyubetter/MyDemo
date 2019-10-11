package com.github.android.sample

import android.os.Parcel
import android.os.Parcelable
import android.widget.ListPopupWindow
import android.widget.PopupWindow

/**
 * 放入package包中
 * 来自任玉刚，开发艺术探索
 */
data class Book(var bookId: Int, var bookName: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bookId)
        parcel.writeString(bookName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
