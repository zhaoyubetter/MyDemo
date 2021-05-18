package com.github.android.sample.ipc.aidl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_communication_each_other.*

/**
 * 相互通信
 */
class CommunicationEachOtherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communication_each_other)
        btn_one.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Communication1Fragment()).commit()
        }
    }
}