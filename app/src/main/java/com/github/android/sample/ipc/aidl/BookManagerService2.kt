package com.github.android.sample.ipc.aidl

import android.app.Service
import android.content.Intent
import com.github.android.sample.Book
import com.github.android.sample.IBookManager
import com.github.android.sample.INewBookAddListener
import java.util.concurrent.CopyOnWriteArrayList

class BookManagerService2 : Service() {

    // AIDL中方法是运行在Binder线程中的，所以采用支持并发读写的 CopyOnWriteArrayList
    private val dataList = CopyOnWriteArrayList<Book>()
    private val listenerList = CopyOnWriteArrayList<INewBookAddListener>()

    // 继承AIDL中的接口
    private val binder = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> = dataList
        override fun addBook(book: Book?) {
            if (book != null) {
                dataList.add(book)
                listenerList.forEach { it ->
                    it.onNewBookAdd(book)
                }
            }
        }

        override fun registerBookAddListener(listener: INewBookAddListener) {
            listenerList.add(listener)
        }
        override fun unregisterBookAddListener(listener: INewBookAddListener) {
            listenerList.remove(listener)
        }
    }

    override fun onBind(intent: Intent?) = binder

    override fun onCreate() {
        super.onCreate()
    }
}