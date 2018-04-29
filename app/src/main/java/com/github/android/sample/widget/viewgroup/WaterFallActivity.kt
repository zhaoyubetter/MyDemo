package com.github.android.sample.widget.viewgroup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.better.base.ToolbarActivity
import com.better.base.e
import com.better.base.toast
import com.github.android.sample.R
import com.github.widget.better.WaterFallLayout
import kotlinx.android.synthetic.main.activity_water_fall.*
import java.util.*

class WaterFallActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_water_fall)

        btn_add.setOnClickListener {
            addItem()
        }

        waterfallLayout.itemClickListener = { _, index ->
            run {
                toast("$index")
            }
        }
    }

    private fun addItem() {
        val layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.topMargin = 30     // TODO: 奇怪设置 topMargin 没有效果
        waterfallLayout.addView(ImageView(this).apply {
            val key = Random().nextInt(6)
            e("$key")
            when (key) {
                0 -> setImageResource(R.mipmap.img_7645)
                1 -> setImageResource(R.mipmap.img_7651)
                2 -> setImageResource(R.mipmap.img_7654)
                3 -> setImageResource(R.mipmap.img_7692)
                4 -> setImageResource(R.mipmap.img_7708)
                else -> {
                    setImageResource(R.mipmap.juntuan)
                }
            }
            scaleType = ImageView.ScaleType.CENTER_CROP
        }, layoutParams)
    }
}
