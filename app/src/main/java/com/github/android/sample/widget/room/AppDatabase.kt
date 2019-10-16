package com.github.android.sample.widget.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [(MobilePhone::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mobilePhoneDao(): MobilePhoneDao
}