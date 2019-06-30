package com.github.android.sample.ipc.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.better.base.e
import com.better.base.getProcessName

/**
 * This broadcast is running in other process.
 */
class ProcessBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        e("收到广播：" + context?.getProcessName())
    }
}