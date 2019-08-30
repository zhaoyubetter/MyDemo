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
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_book_manager.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class BookManagerActivity : ToolbarActivity() {

    private var isConnected = false
    private var bookManager: IBookManager? = null

    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            isConnected = true
            bookManager = IBookManager.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_manager)
        setTitleFromIntent(intent)

        btn_bind.setOnClickListener {
            //            bindService(Intent(baseContext, BookManagerService::class.java), serviceConn, Context.BIND_AUTO_CREATE)
            bindService(Intent("com.github.android.sample.ipc.aidl.bookService")    // 通过yion'shi
                    .setPackage(this@BookManagerActivity.packageName),
                    serviceConn, Context.BIND_AUTO_CREATE)
        }

        // 获取所有书
        btn_getAll.setOnClickListener {
            val allBook = this@BookManagerActivity.getAllBook()
            e(allBook?.javaClass?.canonicalName ?: "")
            allBook?.let {
                allBook.forEach { e(it.toString()) }
            }
        }

        btn_add.setOnClickListener {
            bookManager?.addBook(Book(5, "Atomic Kotlin"))
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
