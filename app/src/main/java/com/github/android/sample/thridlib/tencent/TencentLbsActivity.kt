package com.github.android.sample.thridlib.tencent

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_tencent_lbs.*

class TencentLbsActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tencent_lbs)

        // 基本使用
        // 定位sdk比较简单
        btn_base_use.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container, LocationUseFragment()).commit()
        }
        // 地图
        btn_base_map.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.container, MapUseFragment()).commit()
        }
    }
}
