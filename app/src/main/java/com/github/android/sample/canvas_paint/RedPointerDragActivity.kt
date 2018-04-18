package com.github.android.sample.canvas_paint

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import com.better.base.ToolbarActivity
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.redPointer.RedPointerView1
import kotlinx.android.synthetic.main.activity_red_pointer_drag.*

class RedPointerDragActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_pointer_drag)

        root_container.removeAllViews()
        root_container.addView(RedPointerView1(this))
    }
}
