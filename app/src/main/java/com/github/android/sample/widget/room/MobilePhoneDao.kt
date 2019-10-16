package com.github.android.sample.widget.room

import androidx.room.*


@Dao
interface MobilePhoneDao {
    @Query("select * from tb_mobilePhone")
    fun getAll(): List<MobilePhone>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(vararg phones: MobilePhone)

    @Delete
    fun delete(phone: MobilePhone)

    @Update
    fun update(phone: MobilePhone)

    @Query("select * from tb_mobilePhone where mobile_name like '%' || :name || '%' limit 1")
    fun query(name: String): MobilePhone?
}