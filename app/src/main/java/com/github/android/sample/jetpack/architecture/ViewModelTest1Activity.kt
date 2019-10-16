package com.github.android.sample.jetpack.architecture

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.better.base.ToolbarActivity
import com.github.android.sample.R

/**
 * ViewModel
 * ViewModel 将视图的数据和逻辑从具有生命周期特性的实体（如 Activity 和 Fragment）中剥离开来。
 * 直到关联的 Activity 或 Fragment 完全销毁时，ViewModel 才会随之消失，也就是说，即使在旋转屏幕导致 Fragment 被重新创建等事件中，
 * 视图数据依旧会被保留。
 * ViewModels 不仅消除了常见的生命周期问题，而且可以帮助构建更为模块化、更方便测试的用户界面。
 */
class ViewModelTest1Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model_test1)
        val chronometerViewModel = ViewModelProviders.of(this).get(ChronometerViewModel::class.java)
    }

    // ViewModel
    class ChronometerViewModel(var startTime: Long = 0) : ViewModel()
}


