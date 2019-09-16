package com.github.android.sample.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.Parcel
import android.os.SystemClock
import com.github.android.sample.Book
import com.github.android.sample.IBookManager
import com.github.android.sample.INewBookAddListener
import java.util.concurrent.CopyOnWriteArrayList

class BookManagerService : Service() {

    // AIDL中方法是运行在Binder线程中的，所以采用支持并发读写的 CopyOnWriteArrayList
    private val dataList = CopyOnWriteArrayList<Book>()

    // 继承AIDL中的接口
    private val binder = object : IBookManager.Stub() {

        override fun getBookList(): MutableList<Book> {
            SystemClock.sleep(3000)
            return dataList
        }
        override fun addBook(book: Book?) {
            if (book != null) {
                dataList.add(book)
            }
        }

        override fun registerBookAddListener(listener: INewBookAddListener?) {
        }

        override fun unregisterBookAddListener(listener: INewBookAddListener?) {
        }
    }

    override fun onBind(intent: Intent?) = binder

    override fun onCreate() {
        super.onCreate()
        dataList.add(Book(1, "Kotlin实战"))
        dataList.add(Book(2, "Java并发编程实战"))
    }
}