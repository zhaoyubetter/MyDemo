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

class BookManager2Activity : ToolbarActivity() {

    private var isConnected = false
    private var bookManager: IBookManager? = null

    private val newBookAddListener = object: INewBookAddListener.Stub() {
        override fun onNewBookAdd(book: Book?) {

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
            bookManager?.addBook(Book(5, "Atomic Kotlin"))
        }

        // 添加监听器
        btn_addListener.onClick {
            bookManager?.registerBookAddListener(newBookAddListener)
        }
    }

    private inline fun getAllBook() = bookManager?.bookList ?: null

    override fun onDestroy() {
        if (isConnected) {
            unbindService(serviceConn)
            isConnected = false
        }
        super.onDestroy()
    }
}
