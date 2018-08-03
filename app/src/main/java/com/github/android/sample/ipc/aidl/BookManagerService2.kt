package com.github.android.sample.ipc.aidl

import android.app.Service
import android.content.Intent
import com.better.base.e
import com.github.android.sample.Book
import com.github.android.sample.IBookManager
import com.github.android.sample.INewBookAddListener
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 问题：
 * 1. 没法移除 listener，这是因为 listener注册到远程，是反序列化重新生成的对象，
 *    与客户端注册的 listener是完全不同的2个对象，所有不能移除
 *    也就是 unregisterBookAddListener() 方法失效；
 *
 *     如何解决呢，我们需要使用 RemoteCallbackList
 */
class BookManagerService2 : Service() {

    // AIDL中方法是运行在Binder线程中的，所以采用支持并发读写的 CopyOnWriteArrayList
    private val dataList = CopyOnWriteArrayList<Book>()
    private val listenerList = CopyOnWriteArrayList<INewBookAddListener>()
    private val isServiceDestroy: AtomicBoolean = AtomicBoolean(false)

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
            // 与服务器是2个不同的listener对象
            e("registerListener ---> $listener,  currentSize: ${listenerList.size}")
        }

        override fun unregisterBookAddListener(listener: INewBookAddListener) {
            if(listenerList.contains(listener)) {
                listenerList.remove(listener)
            } else {
                e("not found listener ---> $listener")
            }
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

        // ==2.也是binder线程中
//        android.os.Handler().postDelayed({
//            e("thread : ${Thread.currentThread().name}")       // main
//            onBookAdd(Book(dataList.size + 1, "IPC进阶。。。"))
//        }, 5000)
    }

    private fun onBookAdd(book:Book) {
        dataList.add(book)
        listenerList.forEach { it ->
            it.onNewBookAdd(book)
        }
    }

    override fun onDestroy() {
        isServiceDestroy.set(false)
        super.onDestroy()
    }

}