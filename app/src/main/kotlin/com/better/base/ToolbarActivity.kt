package com.better.base

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.github.android.sample.R
import org.jetbrains.anko.dip

/**
 * 参考cz，拥有 toolbar
 */
open class ToolbarActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    private lateinit var indeterminate: ProgressBar
    /**
     * toolbarLayout对应资源
     */
    private var toolBarLayout = R.layout.common_layout_toolbar
    /**
     * 是否漂浮
     */
    private var overFlow: Boolean = false

    override fun setContentView(layoutResID: Int) {
        val layout = RelativeLayout(this)
        // 初始化content
        val contentView = layoutInflater.inflate(layoutResID, layout, false)
        val contentLp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        if (!overFlow) {
            contentLp.addRule(RelativeLayout.BELOW, R.id.toolbar)
        }
        layout.addView(contentView, contentLp)

        //初始化toolbar
        toolbar = layoutInflater.inflate(toolBarLayout, layout, false) as Toolbar

        //初始化indeterminate
        indeterminate = ProgressBar(this, null, R.attr.progressBarStyle)
        indeterminate.setPadding(dip(12f), dip(12f), dip(12f), dip(12f))
        indeterminate.visibility = View.GONE
        val progressLp = Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT)
        progressLp.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        toolbar.addView(indeterminate, progressLp)
        layout.addView(toolbar)
        initToolbar()
        setContentView(layout)
    }

    private inline fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }
    }

    fun setNavigationOnClickListener(listener: View.OnClickListener) {
        toolbar.setNavigationOnClickListener(listener)
    }

    /**
     * 设置toolbar悬浮,必须在setContentView方法之前回调生效
     * @param overFlow
     */
    fun setToolBarOverFlow(overFlow: Boolean) {
        this.overFlow = overFlow
    }

    /**
     * 设置自定义的toolbar布局对象,必须在setContentView方法之前回调生效
     * @param layout
     */
    fun setCustomToolBar(@LayoutRes layout: Int) {
        this.toolBarLayout = layout
    }

    override fun setTitle(@StringRes res: Int) {
        super.setTitle(res)
        toolbar.setTitle(res)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        toolbar.setTitle(title)
    }

    fun setLogo(@DrawableRes res: Int) {
        toolbar.setLogo(res)
    }

    fun setLogo(drawable: Drawable) {
        toolbar.logo = drawable
    }

    fun setNavigationIcon(@DrawableRes res: Int) {
        toolbar.setLogo(res)
    }

    fun setNavigationIcon(drawable: Drawable) {
        toolbar.navigationIcon = drawable
    }

    /**
     * 是否显示加载旋转框
     * @param showIndeterminate
     */
    fun showIndeterminate(showIndeterminate: Boolean) {
        indeterminate.visibility = if (showIndeterminate) View.VISIBLE else View.GONE
    }


}
