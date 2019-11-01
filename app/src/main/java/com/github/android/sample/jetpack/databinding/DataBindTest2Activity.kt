package com.github.android.sample.jetpack.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.github.android.sample.R
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import com.better.base.toast


/**
 * ObservableField 的使用
 * 耦合太高了。。。
 *
 * 双向绑定
 */
class DataBindTest2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databind: com.github.android.sample.databinding.ActivityDataBindTest2Binding =
                setContentView(this, R.layout.activity_data_bind_test2)

        val goods = ObservableGoods("Android Kotlin", "好书一本", 33.5f)
        databind.goods = goods

        window.decorView.postDelayed({
            goods.name.set("Switch 游戏机")
            goods.details.set("Switch 游戏时间到")
        }, 1000)


        // ===== 测试双向绑定，对了，这个是不能双向绑定的；
        val userVO = UserVO("better", "123")
        databind.userVO = userVO
    }

    // 属性立即更新到UI
    class ObservableGoods(
            name: String = "",
            detail: String = "",
            price: Float = 0F
    ) {
        var name: ObservableField<String> = ObservableField(name)
        var price: ObservableFloat = ObservableFloat(price)
        var details: ObservableField<String> = ObservableField(detail)
    }


    // sample data
    data class UserVO(var name: String = "", var pwd: String = "")
}
