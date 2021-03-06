package com.github.android.sample.canvas_paint


import android.os.Bundle
import android.widget.Toolbar
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.redPointer.RedPointerView1
import com.github.android.sample.canvas_paint.redPointer.RedPointerView2
import com.github.android.sample.canvas_paint.redPointer.RedPointerView3
import com.github.android.sample.canvas_paint.redPointer.RedPointerView4
import kotlinx.android.synthetic.main.activity_red_pointer_drag.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class RedPointerDragActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_pointer_drag)
        setTitleFromIntent(intent)


        root_container.removeAllViews()
        root_container.addView(RedPointerView1(this@RedPointerDragActivity))

        btn_base.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(RedPointerView1(this@RedPointerDragActivity))
        }

        btn_base_2.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(RedPointerView2(this@RedPointerDragActivity))
        }

        btn_base_3.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(RedPointerView3(this@RedPointerDragActivity))
        }

        btn_base_4.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(RedPointerView4(this@RedPointerDragActivity))
        }
    }


}
