package com.github.android.sample.ipc.resultreceiver

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import com.github.android.sample.ipc.resultreceiver.BankResultReceiver.ResultReceiverCallback
import com.github.android.sample.ipc.resultreceiver.BankService.Companion.startServiceForBalance
import com.github.android.sample.ipc.resultreceiver.BankService.Companion.startServiceToDeposit
import com.github.android.sample.ipc.resultreceiver.BankService.Companion.startServiceToWithdraw
import kotlinx.android.synthetic.main.activity_result_receiver_test1.*
import org.jetbrains.anko.toast

/**
 * ResultReceiver 例子
 * 例子来自：
 * https://proandroiddev.com/intentservice-and-resultreceiver-70de71e5e40a
 */
class ResultReceiverTest1Activity : ToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_receiver_test1)
        info()

        btn_in.setOnClickListener { saveMoney() }

        btn_get.setOnClickListener { getMoney() }
    }

    private fun getMoney() {
        startServiceToWithdraw(this, 100, object : ResultReceiverCallback<Any> {
            override fun onSuccess(data: Any) {
                toast("取好了")
                txt.text = null
            }
            override fun onError(e: Exception) {
                txt.text = e.message
            }
        })
    }

    private fun saveMoney() {
        startServiceToDeposit(this, 50, object : ResultReceiverCallback<Any> {
            override fun onSuccess(data: Any) {
                toast("存好了")
                txt.text = null
            }
            override fun onError(e: Exception) {
                txt.text = e.message
            }
        })
    }

    /**
     * 获取信息,注意这里容易出现内存泄露，
     * 正常的做法，参考网站链接
     */
    private fun info() {
        startServiceForBalance(this, object : ResultReceiverCallback<Any> {
            override fun onSuccess(data: Any) {
                txt.text = data.toString()
            }

            override fun onError(e: Exception) {
                txt.text = e.message
            }
        })
    }
}
