package com.github.android.sample.concurrent

import android.os.Bundle
import android.util.Log
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import com.github.android.sample.tools.WorkThread
import kotlinx.android.synthetic.main.activity_work_thread.*
import kotlin.random.Random

class WorkThreadActivity : ToolbarActivity() {

    val workThread = WorkThread<String>("data-operate")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_thread)

        workThread.startService()

        workThread.addObserver { o, arg ->
            Log.i("better ${Thread.currentThread().name}", arg as String)
        }

        btn1_t1.setOnClickListener {
            workThread.post("data1 --> ${Random(10).nextInt()}")
        }

        btn1_t2.setOnClickListener {
            workThread.post("data2 --> ${Random(10).nextInt()}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        workThread.stopService()
    }
}
