package com.github.android.sample.problem.memory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.android.sample.R
import com.github.android.sample.tools.ImageUtils
import kotlinx.android.synthetic.main.activity_memory_shake.*

class MemoryBitmapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_shake)

        btn_load_img.setOnClickListener {
            iv_img.setImageBitmap(ImageUtils.getOriginBitmap(applicationContext, R.mipmap.animal_));
        }

        btn_load_img1.setOnClickListener {
            iv_img.setImageBitmap(ImageUtils.getScaleBitmap(applicationContext, R.mipmap.animal_, btn_load_img1));
        }
    }
}