package com.github.android.sample.jetpack.architecture

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import com.github.android.sample.jetpack.architecture.frag.ViewModelFrag1
import com.github.android.sample.jetpack.architecture.frag.ViewModelFrag2
import kotlinx.android.synthetic.main.activity_view_model_test1.*


/**
 * ViewModel 例子来自：
 * https://github.com/googlecodelabs/android-lifecycles/blob/master/app/src/main/java/com/example/android/lifecycles/step2/ChronoActivity2.java
 *
 * https://blog.csdn.net/c10WTiybQ1Ye3/article/details/104568397
 *
 * ViewModel 将视图的数据和逻辑从具有生命周期特性的实体（如 Activity 和 Fragment）中剥离开来。
 * 直到关联的 Activity 或 Fragment 完全销毁时，ViewModel 才会随之消失，也就是说，即使在旋转屏幕导致 Fragment 被重新创建等事件中，
 * 视图数据依旧会被保留。
 * ViewModels 不仅消除了常见的生命周期问题，而且可以帮助构建更为模块化、更方便测试的用户界面。
 */
class ViewModelTest1Activity : ToolbarActivity() {

    val TAG = "frag2";
    private var frag2: Fragment? = null

    // activity 重建时，onCreate 会走
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model_test1)
        if (savedInstanceState != null) {
            frag2 = supportFragmentManager.findFragmentByTag(TAG)
        }

        // remove frag2, 移调了viewmodel中的数据才会删除；
        btn_destroy.setOnClickListener {
            if(frag2 != null) {
                supportFragmentManager.beginTransaction().remove(frag2!!).commit()
            }
        }

        btn_google.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container, ViewModelFrag1()).commit()
        }

        btn_1.setOnClickListener {
            if (frag2 == null) {
                frag2 = ViewModelFrag2()
            }
            supportFragmentManager.beginTransaction().replace(R.id.container, frag2!!, TAG).commit()
        }
    }
}


