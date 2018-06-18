package com.github.android.sample.widget.recyler

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.better.base.ToolbarActivity
import com.better.base.setTitleFromIntent
import com.github.android.sample.R
import com.github.android.sample.widget.recyler.fragment.*
import org.jetbrains.anko.find


class RecyclerViewBaseUseActivity : ToolbarActivity(), NavigationDrawerFragment.NavigationDrawerCallbacks {

    lateinit var navigationDrawerFragment: NavigationDrawerFragment
    lateinit var oldTitle: CharSequence

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view1)
        setTitleFromIntent(intent)

        oldTitle = "" + supportActionBar?.title
        navigationDrawerFragment = supportFragmentManager.findFragmentById(R.id.navigation_drawer) as NavigationDrawerFragment
        navigationDrawerFragment.setUp(R.id.navigation_drawer, find(R.id.drawer_layout))

    }

    override fun onNavigationDrawerItemSelected(position: Int) {
        // update the main content by replacing fragments
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        when (position) {
            0 -> ft.replace(R.id.container, VerticalFragment.newInstance())
            1 -> ft.replace(R.id.container, HorizontalFragment.newInstance())
            2 -> ft.replace(R.id.container, VerticalGridFragment.newInstance())
            3 -> ft.replace(R.id.container, VerticalStaggeredGridFragment.newInstance())
            4 -> ft.replace(R.id.container, FixedTwoWayFragment.newInstance())
            else -> {
            }
        }//Do nothing

        ft.commit()
    }

    private fun restoreActionBar() {
        supportActionBar?.apply {
            this.setDisplayShowTitleEnabled(true)
            this.title = oldTitle
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar()
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
