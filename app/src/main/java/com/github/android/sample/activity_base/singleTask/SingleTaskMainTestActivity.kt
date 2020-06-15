package com.github.android.sample.activity_base.singleTask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_single_task_test.*

/**
 *  如果Intent添加flags = Intent.FLAG_ACTIVITY_NEW_TASK则需要跟 taskAffinity 配合起来使用，
 *  单独配置 taskAffinity 无效果；
 *  同理如果配置launchMode为singleTask，如果不配置 taskAffinity 则不会启动新的任务栈；
 *  可见 taskAffinity 属性是多么重要；
 *
 *  实验证明：
 *  前提：如果启动Intent添加flags = Intent.FLAG_ACTIVITY_NEW_TASK：
 *  如果对方activity配置了launchMode为singleTask，则新栈中只能出现一个activity示例；
 *  如果没配置launchMode为singleTask，跟普通Activity没区别了；
 *
 *
 *
 *
 *  Intent.FLAG_ACTIVITY_NEW_TASK的作用：
 *  如果不配置 taskAffinity 属性，则不会创建新的任务栈，直接使用parent的任务栈；
 *  如果使用applicationContext来启动目标，由于applicationContext没有任务栈，所以需要添加 Intent.FLAG_ACTIVITY_NEW_TASK，
 *  此时新的任务栈为包名；
 *
 *
 *
 *  launchMode为singleTask的作用仅仅只是：
 *  在启动一个singleTask的Activity实例时，如果系统中已经存在这样一个实例，
 *  就会将这个实例调度到任务栈的栈顶，并清除它当前所在任务中位于它上面的所有的activity。
 *
 *
 *  onActivityResult() 返回失败问题
 *  如果传递 Intent.FLAG_ACTIVITY_NEW_TASK onActivityResult将立刻执行，不会再 目标activityi关闭后执行；
 *
 *  ======= 补充学习资料：=======
 *  查看任务栈命令: adb shell dumpsys activity activities (搜索： Running activities (most recent first):)
 *  1. ActivityRecord : Activity 实例与 ActivityRecord 一一对应
 *      packageName, processName, taskAffinity 等；
 *  2. TaskRecord : 记录 Activity 开启的先后顺序，里面的 Activity 顺序不能动
 *      ArrayList<ActivityRecord> mActivities;
 *  3. ActivityStack : Activity 任务栈，维护 TaskRecord 列表，列表中的 TaskRecord 可以重排序；
 *      ArrayList<TaskRecord> mTaskHistory
 *  ==》ActivityStack 管理 TaskRecord, TaskRecord 管理 ActivityRecord; ActivityStack Activity 任务栈发挥着调度的作用；
 *  ==》ActivityStack 表示亲和性的意思
 *
 *  ========= 自我总结 ======
 *  1. 如果在一个 activity A （taskAffinity 为 tA） 上打开其他 activity 如 B (taskAffinity 为包名，未配置launchModel为singleTask)，
 *         那么 B 则会在 A 的任务栈里头，即：taskAffinity 单独使用无效；
 *  2. 如果打开B时（B Affinity 配置了，但launchModel未配置），添加 Intent.FLAG_ACTIVITY_NEW_TASK 标记，则 B 会回到自己的任务栈里头
 *      （即：taskAffinity 为包名的任务栈），如果任务栈名存在，
 *     这就会造成 B 返回时，栈返回问题，此时的 Intent.FLAG_ACTIVITY_NEW_TASK 与 清单配置 singleTask 的作用相同；
*   3. singTask 并且配置了 affinity 独立的 Activity B 启动 另外一个 singleTask 的 Activity C，C 返回时，会发生什么情况
 *     C 返回到启动 B 的 Activity，如何解决，可使用 startActivityForResult 启动 C；
 *  4. 只有 配置了 singleTask 模式的 activity 才会发生返回栈问题；
 *  5. startActivity 时传递 Intent.FLAG_ACTIVITY_NEW_TASK（对方Activity必须配置了taskAffinity） 跟配置 singleTask 的作用相同
 */
class SingleTaskMainTestActivity : ToolbarActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_task_test)

        // singleTask
        button.setOnClickListener {
            startActivity(Intent(SingleTaskMainTestActivity@ this, SingleTaskActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK  // 如果在不配置 taskAffinity 属性，并没有创建新的任务栈；
            })
        }

        // singleTask
        // singTask 并且配置了 affinity 独立的 Activity B 启动 另外一个 singleTask 的 Activity C，C 返回时，会发生什么情况
        btn_singleTask.setOnClickListener {
            startActivity(Intent(SingleTaskMainTestActivity@ this, SingleTask2Activity::class.java).apply {
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
