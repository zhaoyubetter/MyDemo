package com.github.android.sample

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.github.android.sample.widget.WidgetContentFragment
//import kotlinx.android.extensions.CacheImplementation
//import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.activity_main.*

//@ContainerOptions(cache = CacheImplementation.NO_CACHE)
class MainActivity : AppCompatActivity() {

    val tabTitle = listOf("基础部分", "进阶", "项目实战")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Demo Kotlin"

        // setting tabLayout
        tabLayout.tabMode = 0
        tabLayout.setupWithViewPager(viewPager)
        // setting content
        val base = BasicContentFragment()
        val improve = WidgetContentFragment()
        val project = WidgetContentFragment()
        val fragments = listOf(base, improve, project)
        val adapter = TabContentAdapter(supportFragmentManager, fragments, tabTitle)
        viewPager.adapter = adapter

        with(viewPager) {
            (0..2).filter { it -> it % 2 ==0 }
        }
    }

    class TabContentAdapter(pm: FragmentManager, val fragments: List<Fragment>, val tabIndicators: List<String>) :
            FragmentPagerAdapter(pm) {
        init {
            if (fragments.size != tabIndicators.size) {
                throw RuntimeException("fragments.size not equals tabIndicators.size")
            }
        }

        override fun getItem(position: Int) = fragments[position]
        override fun getCount() = fragments.size
        override fun getPageTitle(position: Int) = tabIndicators[position]

        val abc = buildString {
            for (alpha in 'A'..'Z') {
                append(alpha)
            }
        }

    }
}