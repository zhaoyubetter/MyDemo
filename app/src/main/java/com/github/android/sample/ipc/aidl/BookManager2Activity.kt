package com.github.android.sample.ipc.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.better.base.ToolbarActivity
import com.better.base.e
import com.better.base.setTitleFromIntent
import com.github.android.sample.Book
import com.github.android.sample.IBookManager
import com.github.android.sample.INewBookAddListener
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_book_manager2.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * 监听
 */
class BookManager2Activity : ToolbarActivity() {

    private var isConnected = false
    private var bookManager: IBookManager? = null

    // binder中调用就是 binder线程池
    /**
     * 客户端调用，就是主进程的main
     * @see BookManager2Activity#onCreate
     */
    private val newBookAddListener = object : INewBookAddListener.Stub() {
        override fun onNewBookAdd(book: Book) {
            e("${book} is add! threadName: ${Thread.currentThread().name}")
        }
    }

    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            isConnected = true
            bookManager = IBookManager.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
            bookManager = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_manager2)
        setTitleFromIntent(intent)

        btn_bind.onClick {
            bindService(Intent(baseContext, BookManagerService2::class.java), serviceConn, Context.BIND_AUTO_CREATE)
        }

        // 获取所有书
        btn_getAll.onClick {
            val allBook = this@BookManager2Activity.getAllBook()
            e(allBook?.javaClass?.canonicalName ?: "")
            allBook?.let {
                allBook.forEach { e(it.toString()) }
            }
        }

        // 添加书
        btn_add.onClick {
            // 这里调用 newBookAddListener 打印的为是主线程
            bookManager?.addBook(Book(5, "Atomic Kotlin"))
        }

        // 添加监听器
        btn_addListener.onClick {
            bookManager?.registerBookAddListener(newBookAddListener)
            e("registerListener $newBookAddListener")
        }
    }

    private inline fun getAllBook() = bookManager?.bookList ?: null

    override fun onDestroy() {
        if (isConnected) {
            unbindService(serviceConn)
            isConnected = false
        }

        // 解除注册
        if(bookManager != null && bookManager?.asBinder()?.isBinderAlive == true) {
            // 解绑不成功 newBookAddListener 会在 远程进程 中被重新反序列化；
            // 怎么办，使用 RemoteCallbackList
            bookManager?.unregisterBookAddListener(newBookAddListener)
        }

        super.onDestroy()
    }
}
