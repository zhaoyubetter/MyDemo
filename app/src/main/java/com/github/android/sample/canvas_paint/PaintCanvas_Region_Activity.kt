package com.github.android.sample.canvas_paint

import android.app.Activity
import android.graphics.Region
import android.os.Bundle
import com.better.base.ToolbarActivity
import com.better.base.model.SampleItem
import com.github.android.sample.R
import com.github.android.sample.canvas_paint.view.MyRegionView1
import com.github.android.sample.canvas_paint.view.MyRegionView2
import com.github.android.sample.canvas_paint.view.MyRegionView3
import kotlinx.android.synthetic.main.activity_paint_canvas__region_.*

class PaintCanvas_Region_Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paint_canvas__region_)

        intent.getParcelableExtra<SampleItem<Activity>>("item")?.let {
            supportActionBar?.title = it.title
            supportActionBar?.subtitle = it.desc
        }

        val regionView = MyRegionView1(this)
        root_container.addView(regionView)

        btn_set_region.setOnClickListener {
            regionView.setRegion(Region(50, 50, 100, 100))
        }

        // setPath
        btn_region_path.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyRegionView2(this))
        }

        // 几个集合
        btn_intersect.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyRegionView3(this).apply { setOp(Region.Op.INTERSECT) })
        }

        btn_union.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyRegionView3(this).apply { setOp(Region.Op.UNION) })
        }

        btn_diff.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyRegionView3(this).apply { setOp(Region.Op.DIFFERENCE) })
        }

        btn_reverse_diff.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyRegionView3(this).apply { setOp(Region.Op.REVERSE_DIFFERENCE) })
        }

        btn_replace.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyRegionView3(this).apply { setOp(Region.Op.REPLACE) })
        }

        btn_xor.setOnClickListener {
            root_container.removeAllViews()
            root_container.addView(MyRegionView3(this).apply { setOp(Region.Op.XOR) })
        }
    }
}
