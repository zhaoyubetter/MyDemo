package com.github.android.sample.jetpack.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.github.android.sample.R
import com.github.android.sample.databinding.ActivityDatabindTest3Binding
import java.util.*

/**
 * BindingAdapter
 */
class DatabindTest3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataBindingUtil: ActivityDatabindTest3Binding
                = DataBindingUtil.setContentView(this, R.layout.activity_databind_test3)
        dataBindingUtil.image = Image("test url...")
        dataBindingUtil.handler = Handler()
    }

    class Image(pUrl: String) {
        val url = ObservableField<String>(pUrl)
    }

    class Handler {
        fun onClick(image: Image) {
            image.url.set("xxxxx" + Random().nextInt(1000))
        }
    }
}
