package com.github.android.sample.jetpack.databinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.Observable
import com.better.base.ToolbarActivity
import com.better.base.toast
import com.github.android.sample.BR
import com.github.android.sample.R
import com.github.android.sample.databinding.ActivityDataBindTest1Binding
import kotlinx.android.synthetic.main.activity_data_bind_test1.*
import java.util.*

/**
 * 简单使用,databing 对布局文件使用有限制，只能一对一使用，ToolbarActivity不能使用了
 * 参考：https://juejin.im/post/5b02cf8c6fb9a07aa632146d#heading-2
 * databinding
 */
class DataBindTest1Activity : AppCompatActivity() {

    private lateinit var goodsVO: Goods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databind: ActivityDataBindTest1Binding = setContentView(this, R.layout.activity_data_bind_test1)
        val userVO = UserVO("better", "123456")
        databind.userVO = userVO

        // 设置 VO 改变，是的数据改变，不会引起UI变化，
        btn_change.setOnClickListener {
            userVO.name = "new name"
            userVO.pwd = "qwert"
        }

        // =========================
        // 单项绑定之 BaseObservable
        goodsVO = Goods()
        databind.goodsVO = this.goodsVO
        databind.goodsHandler = GoodsHandler()

        // ===== 注册接口，监听属性变化
        goodsVO.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                when(propertyId) {
                    BR.name -> toast("name 属性更新")
                    BR._all -> toast("全部属性更新")
                }
            }
        })
    }

    //////////////// vo
    data class UserVO(var name: String = "", var pwd: String = "")

    ///// 单项数据绑定
    class Goods(
            name: String = "",
            detail: String = "",
            price: Float = 0F) : BaseObservable() {

        @Bindable
        var name: String = name
            set(value) {
                field = value
                // 只更新单个字段
                notifyPropertyChanged(com.github.android.sample.BR.name)
            }

        @Bindable
        var detail = detail
            set(value) {
                field = value
                notifyChange()  // 全部更新
            }
        var price = price
    }

    inner class GoodsHandler {
        fun changeGoodsName() {
            toast("ok")
            goodsVO.name = "Switch 游戏机 ${Random().nextInt(100)}"
            goodsVO.price = Random().nextInt(100).toFloat()
        }

        fun changeGoodsDetail() {
            goodsVO.detail = "红蓝机器，${Random().nextInt(100)}"
            goodsVO.price = Random().nextInt(100).toFloat()
        }
    }

}
