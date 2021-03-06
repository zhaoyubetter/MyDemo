package com.github.android.sample.widget.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "tb_mobilePhone")
data class MobilePhone(
        @ColumnInfo(name = "mobile_name")
        var name: String = "",
        var price: Double = 0.toDouble(),
        var height: Int = 0, var width: Int = 0) {

    @Ignore
    constructor() : this(name = "")

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


}


