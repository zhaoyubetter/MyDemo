package com.github.android.sample.provider.db

import com.github.android.sample.provider.db.annotations.Table

/**
 * Created by better On 2019-09-05.
 */
@Table("tb_user", primaryKey = "_id", autoIncrement = true)
data class User(
        var name: String = "",
        var age: Int = 0)