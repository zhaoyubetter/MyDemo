package com.github.android.sample.room

import android.arch.persistence.room.Room
import android.content.Context
import android.support.test.InstrumentationRegistry
import com.github.android.sample.widget.room.AppDatabase
import com.github.android.sample.widget.room.MobilePhone
import com.github.android.sample.widget.room.MobilePhoneDao
import org.junit.Before
import org.junit.Test

class RoomTest1 {

    lateinit var db: AppDatabase
    lateinit var ctx: Context
    lateinit var mobilePhoneDao: MobilePhoneDao

//    val db: AppDatabase by lazy {
//        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "roomTest.db").build()
//    }

    @Before
    fun before() {
        ctx = InstrumentationRegistry.getTargetContext()
        db = Room.databaseBuilder(ctx, AppDatabase::class.java, "roomTest.db").build()
        mobilePhoneDao = db.mobilePhoneDao()
        println(db.isOpen)
    }

    @Test
    fun add() {
        val phone = MobilePhone(name = "小米", price = 5000.0, width = 1080, height = 2048)
        println(mobilePhoneDao.add(phone))
    }

    @Test
    fun getAll() {
        val list = mobilePhoneDao.getAll()
        println(list?.size)
    }
}