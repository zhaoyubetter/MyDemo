package com.github.android.sample.widget.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(MobilePhone::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mobilePhoneDao(): MobilePhoneDao
}