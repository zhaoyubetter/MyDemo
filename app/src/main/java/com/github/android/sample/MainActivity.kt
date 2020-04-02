package com.github.android.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.android.sample.problem.ProblemContentFragment
import com.github.android.sample.thridlib.ThirdLibFragment
import com.github.android.sample.widget.WidgetContentFragment
import kotlinx.android.synthetic.main.activity_main.*

//@ContainerOptions(cache = CacheImplementation.NO_CACHE)
class MainActivity : AppCompatActivity() {

    val tabTitle = listOf("基础部分", "进阶", "问题", "第三方lib")

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
        val problem = ProblemContentFragment()
        val project = ThirdLibFragment()
        val fragments = listOf<Fragment>(base, improve, problem, project)
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