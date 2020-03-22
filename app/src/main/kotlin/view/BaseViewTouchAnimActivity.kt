package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R

/**
 * View onTouchEvent 处理
 */
class BaseViewTouchAnimActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_view_anim)
    }
}
