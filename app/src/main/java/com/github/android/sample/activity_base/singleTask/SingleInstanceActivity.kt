package com.github.android.sample.activity_base.singleTask

import android.content.Intent
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_single_instance.*

/**
 * 参考：https://blog.csdn.net/ljz2009y/article/details/26621815
 * 独自使用一个任务栈
 *
 * 1. singleInstance模式的Activity总是会在新的任务中运行(前提是系统中还不存在这样的一个实例) ，并且是全局单利。
 * 2. 以singleInstance模式启动的Activity具有独占性，即它会独自占用一个任务，被他开启的任何activity都会运行在其他任务中
 *    注意：打开的activity，按返回键，如果此时默认任务有其他activity，将回到默认任务栈，而不是singleInstance Activity；
 *    没有则回到singleInstance模式的Activity；
 *
 * 3.
 */
class SingleInstanceActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_instance)


        text.text = "${text.text} ${taskId}"

        //
        button2.setOnClickListener {
            startActivity(Intent(SingleInstanceActivity@ this, OtherActivity::class.java))
        }

        setTitle("singleInstace")
    }
}
