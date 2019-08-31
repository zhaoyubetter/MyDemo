package com.github.android.sample.activity_base.singleTask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_single_task_test.*

/**
 *  如果Intent添加flags = Intent.FLAG_ACTIVITY_NEW_TASK则需要跟 taskAffinity 配合起来使用；
 *  同理如果配置launchMode为singleTask，如果不配置 taskAffinity 则不会启动新的任务栈；
 *  可见 taskAffinity 属性是多么重要；
 *
 *  实验证明：
 *  前提：如果启动Intent添加flags = Intent.FLAG_ACTIVITY_NEW_TASK：
 *  如果对方activity配置了launchMode为singleTask，则新栈中只能出现一个activity示例；
 *  如果没配置launchMode为singleTask，跟普通Activity没区别了；
 *
 *  Intent.FLAG_ACTIVITY_NEW_TASK的作用：
 *  如果不配置 taskAffinity 属性，则不会创建新的任务栈，直接使用parent的任务栈；
 *  如果使用applicationContext来启动目标，由于applicationContext没有任务栈，所以需要添加 Intent.FLAG_ACTIVITY_NEW_TASK，
 *  此时新的任务栈为包名；
 *
 *
 *  launchMode为singleTask的作用仅仅只是：
 *  在启动一个singleTask的Activity实例时，如果系统中已经存在这样一个实例，
 *  就会将这个实例调度到任务栈的栈顶，并清除它当前所在任务中位于它上面的所有的activity。
 */
class SingleTaskMainTestActivity : ToolbarActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_task_test)

        // singleTask
        button.setOnClickListener {
            startActivity(Intent(SingleTaskMainTestActivity@ this, SingleTaskActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK  // 如果在不配置 taskAffinity 属性，并没有创建新的任务栈；
            })
        }

        // singleInstance
        btn_singleinstance.setOnClickListener {
            startActivity(Intent(SingleTaskMainTestActivity@ this, SingleInstanceActivity::class.java).apply {
            })
        }

        // normal
        button3.setOnClickListener {
            startActivity(Intent(SingleTaskMainTestActivity@ this, OtherActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK  // 如果在不配置 taskAffinity 属性，并没有创建新的任务栈；
            })
        }

        text.text = "${text.text} ${taskId}"
    }
}
