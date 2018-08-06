package com.github.android.sample.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.RemoteCallbackList
import com.better.base.e
import com.github.android.sample.Book
import com.github.android.sample.IBookManager
import com.github.android.sample.INewBookAddListener
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * RemoteCallbackList 使用
 */
class BookManagerService3 : Service() {

    // AIDL中方法是运行在Binder线程中的，所以采用支持并发读写的 CopyOnWriteArrayList
    private val dataList = CopyOnWriteArrayList<Book>()
    private val listenerList = RemoteCallbackList<INewBookAddListener>()
    private val isServiceDestroy: AtomicBoolean = AtomicBoolean(false)

    // 继承AIDL中的接口
    private val binder = object : IBookManager.Stub() {
        override fun getBookList(): MutableList<Book> = dataList
        override fun addBook(book: Book?) {
            if (book != null) {
                dataList.add(book)

                val size = listenerList.beginBroadcast()
                (0 until size).map { listenerList.getBroadcastItem(it) }.forEach {
                    it?.onNewBookAdd(book)
                }
                listenerList.finishBroadcast()
            }
        }

        override fun registerBookAddListener(listener: INewBookAddListener) {
            listenerList.register(listener)
        }

        override fun unregisterBookAddListener(listener: INewBookAddListener) {
            e("unregisterBookAddListener")       // main
            listenerList.unregister(listener)
        }
    }

    override fun onBind(intent: Intent?) = binder

    override fun onCreate() {
        super.onCreate()

        // ==1. binder线程
        Thread(Runnable {
            while (!isServiceDestroy.get()) {
                Thread.sleep(3000)
                onBookAdd(Book(dataList.size + 1, "IPC进阶。。。"))
            }
        }).start()

        // ==2.客户端打印的是binder线程
//        android.os.Handler().postDelayed({
//            e("-=======》thread : ${Thread.currentThread().name}")       // main
//            onBookAdd(Book(dataList.size + 1, "IPC进阶。。。"))
//        }, 5000)
    }

    private fun onBookAdd(book: Book) {
        dataList.add(book)
        val size = listenerList.beginBroadcast()
        (0 until size).map { listenerList.getBroadcastItem(it) }.forEach {
            it?.onNewBookAdd(book)
        }
        listenerList.finishBroadcast()
    }

    override fun onDestroy() {
        isServiceDestroy.set(false)
        super.onDestroy()
    }

}