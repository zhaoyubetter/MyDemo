package com.github.android.sample.jetpack.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import com.github.android.sample.databinding.ActivityDataBindTest1Binding

/**
 * 简单使用
 * 参考：https://juejin.im/post/5b02cf8c6fb9a07aa632146d#heading-2
 * databinding
 */
class DataBindTest1Activity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databind: ActivityDataBindTest1Binding = setContentView(this, R.layout.activity_data_bind_test1)
        databind.userVO = UserVO("better", "123456")
    }

    //////////////// vo
    data class UserVO(var name: String = "", var pwd: String = "")
}
