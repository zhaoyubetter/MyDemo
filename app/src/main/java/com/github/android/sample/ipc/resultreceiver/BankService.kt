package com.github.android.sample.ipc.resultreceiver

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.ResultReceiver
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log


/**
 * @author zhaoyu1  2019/5/22
 **/
class BankService : IntentService("bankService") {

    companion object {
        var balance = 10

        /**
         * 获取余额
         */
        fun startServiceForBalance(context: Context, callback: BankResultReceiver.ResultReceiverCallback<Any>) {
            val resultReceiver = BankResultReceiver(Handler())
            resultReceiver.callback = callback

            context.startService(Intent(context,
                    BankService::class.java).apply {
                action = Actions.BALANCE.name
                putExtra(PARAM.RESULT_RECEIVER.name, resultReceiver)
            })
        }

        /**
         * 存
         */
        fun startServiceToDeposit(context: Context, amount: Int, callback: BankResultReceiver.ResultReceiverCallback<Any>) {
            val resultReceiver = BankResultReceiver(Handler())
            resultReceiver.callback = callback

            context.startService(Intent(context, BankService::class.java).apply {
                action = Actions.DEPOSIT.name
                putExtra(PARAM.AMOUNT.name, 50)
                putExtra(PARAM.RESULT_RECEIVER.name, resultReceiver)
            })
        }

        /**
         * 取，重复代码有点多
         */
        fun startServiceToWithdraw(context: Context, amount: Int, callback: BankResultReceiver.ResultReceiverCallback<Any>) {
            val resultReceiver = BankResultReceiver(Handler())
            resultReceiver.callback = callback

            context.startService(Intent(context, BankService::class.java).apply {
                action = Actions.WITHDRAW.name
                putExtra(PARAM.AMOUNT.name, 100)
                putExtra(PARAM.RESULT_RECEIVER.name, resultReceiver)
            })
        }
    }

    private enum class Actions {
        BALANCE, DEPOSIT, WITHDRAW
    }

    private enum class PARAM {
        AMOUNT, RESULT_RECEIVER
    }


    override fun onHandleIntent(intent: Intent) {
        // 子线程
        Log.e("better", "thread name : ${Thread.currentThread().name}")
        // 1. get ResultReceiver object from intent
        val resultReceiver = intent.getParcelableExtra<ResultReceiver>(PARAM.RESULT_RECEIVER.name)
        when (intent.action) {
            Actions.BALANCE.name -> {
                handleRetreiveBalance(resultReceiver)
            }
            Actions.DEPOSIT.name -> {  // 存钱
                handleDeposit(resultReceiver, intent.getIntExtra(PARAM.AMOUNT.name, 0))
            }
            Actions.WITHDRAW.name -> {  // 取
                handleWithdraw(resultReceiver, intent.getIntExtra(PARAM.AMOUNT.name, 0))
            }
        }
    }

    private fun handleWithdraw(resultReceiver: ResultReceiver, amount: Int) {
        val bundle = Bundle()
        var code = 0
        SystemClock.sleep(500)
        if (balance < amount) {
            code = BankResultReceiver.RESULT_CODE_ERROR
            bundle.putSerializable(BankResultReceiver.PARAM_EXCEPTION, Exception("余额不足！"))
        } else {
            code = BankResultReceiver.RESULT_CODE_OK
            balance -= amount
            bundle.putBoolean(BankResultReceiver.PARAM_RESULT, true)
        }
        resultReceiver.send(code, bundle)
    }

    /**
     * 存
     */
    private fun handleDeposit(resultReceiver: ResultReceiver, amount: Int) {
        val bundle = Bundle()
        var code = 0
        SystemClock.sleep(200)
        if (balance < 0) {
            code = BankResultReceiver.RESULT_CODE_ERROR
            bundle.putSerializable(BankResultReceiver.PARAM_EXCEPTION, Exception("不能存负数！"))
        } else {
            code = BankResultReceiver.RESULT_CODE_OK
            balance += amount
            bundle.putBoolean(BankResultReceiver.PARAM_RESULT, true)
        }
        resultReceiver.send(code, bundle)
    }

    private fun handleRetreiveBalance(resultReceiver: ResultReceiver) {
        val bundle = Bundle()
        val code = BankResultReceiver.RESULT_CODE_OK
        //Just add sleep to simulate network latency
        SystemClock.sleep(500)
        bundle.putSerializable(BankResultReceiver.PARAM_RESULT, balance)
        resultReceiver.send(code, bundle)  // 通过 receiver 发送给调用方
    }

}