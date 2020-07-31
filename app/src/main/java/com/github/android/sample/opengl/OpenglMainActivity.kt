package com.github.android.sample.opengl

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_opengl_main.*

/**
 * http://pragprog.com/book/kbogla
 */
class OpenglMainActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl_main)

        // first
        btn_first.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.content, FirstOpenglFragment()).commit()
        }
        // second
        btn_two.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.content, SecondOpenglFragment()).commit()
        }
    }
}
