package view

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R

class BaseDispatchTouchEventActivity : ToolbarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_dispatch_touch_event)
    }
}