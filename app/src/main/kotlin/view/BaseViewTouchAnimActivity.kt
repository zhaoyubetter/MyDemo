package view

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.better.base.ToolbarActivity
import com.better.base.toast
import com.github.android.sample.R
import kotlinx.android.synthetic.main.activity_base_view_anim.*
import org.jetbrains.anko.backgroundColor
import view.testView.PullScrollView

/**
 * View onTouchEvent 处理
 */
class BaseViewTouchAnimActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_view_anim)

        // 1.阻尼回弹
        btn_base.setOnClickListener {
            container.removeAllViews()
            container.addView(layoutInflater.inflate(R.layout.auto_scroll_back_view, null))
        }

        // 2.下拉view
        btn_pullDown.setOnClickListener {
            showPullDown()
        }
    }

    private fun showPullDown() {
        container.removeAllViews()
        container.addView(layoutInflater.inflate(R.layout.pull_down_view, null))
        val tableLayout = container.findViewById<TableLayout>(R.id.tableLayout)
        val pullView = container.findViewById<PullScrollView>(R.id.pullView)
        pullView.setHeadView(container.findViewById(R.id.headerView))

        val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        layoutParams.leftMargin = 30
        layoutParams.bottomMargin = 10
        layoutParams.topMargin = 10
        // add data
        (0..30).map { index ->
            TableRow(this).apply {
                addView(TextView(context).apply {
                    text = ("Test pull down scroll view : $index")
                }, layoutParams)
                backgroundColor = if (index % 2 == 0) Color.LTGRAY else Color.WHITE
                setOnClickListener {
                    toast("Click item ${index}")
                }
            }
        }.map { tableLayout.addView(it) }
    }
}
