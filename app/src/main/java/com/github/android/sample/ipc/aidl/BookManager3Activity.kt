package com.github.android.sample.ipc.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.better.base.ToolbarActivity
import com.better.base.e
import com.better.base.setTitleFromIntent
import com.github.android.sample.Book
import com.github.android.sample.IBookManager
import com.github.android.sample.INewBookAddListener
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_book_manager3.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 *
 */
class BookManager3Activity : ToolbarActivity() {

    var bookManager: IBookManager? = null
    var isConnection = false

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            bookManager = IBookManager.Stub.asInterface(service)
            isConnection = true
            // auto register
            bookManager?.registerBookAddListener(newBookAddListener)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bookManager = null
            isConnection = false
        }
    }

    /**
     * 监听新书添加监听器
     */
    val newBookAddListener = object: INewBookAddListener.Stub() {
        override fun onNewBookAdd(book: Book?) {
            e("${book} is add! threadName: ${Thread.currentThread().name}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_manager3)
        setTitleFromIntent(intent)

        // bind service
        btn_bind.onClick {
            bindService(Intent(this@BookManager3Activity, BookManagerService3::class.java),
                    serviceConnection, Context.BIND_AUTO_CREATE)
        }

        // 添加书
        btn_add.onClick {
            // 客户端主动调用，此时回调打印的主进程
            /** @see BookManager3Activity.newBookAddListener */
            //bookManager?.addBook(Book(5, "Atomic Kotlin"))

            // new thread to add a book, 那么回调也是在子线程。注意：这是是线程；那么回调此进程对应的子线程中。
            Thread {
                e("====>>thread add book, " + Thread.currentThread().name)
                bookManager?.addBook(Book(5, "Atomic Kotlin"))
            }.start()
        }

        // btn_remove，移除监听
        btn_remove.onClick {
            bookManager?.unregisterBookAddListener(newBookAddListener)
        }
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        isConnection = false
        super.onDestroy()
    }
}
