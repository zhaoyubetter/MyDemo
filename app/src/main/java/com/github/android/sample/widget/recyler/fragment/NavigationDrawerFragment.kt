package com.github.android.sample.widget.recyler.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.github.android.sample.R
import org.jetbrains.anko.listView
import android.content.Context
import android.content.res.Configuration
import android.view.MenuItem

/**
 * 抽屉导航
 */
class NavigationDrawerFragment : Fragment() {
    private val STATE_SELECTED_POSITION = "selected_navigation_drawer_position"
    private val PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned"

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private var mCallbacks: NavigationDrawerCallbacks? = null

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerListView: ListView? = null
    private lateinit var mFragmentContainerView: View

    private var mCurrentSelectedPosition = 0
    private var mFromSavedInstanceState: Boolean = false
    private var mUserLearnedDrawer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false)

        savedInstanceState?.let {
            mCurrentSelectedPosition = it.getInt(STATE_SELECTED_POSITION)
            mFromSavedInstanceState = true
        }

        selectItem(mCurrentSelectedPosition)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDrawerListView = inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false) as ListView
        mDrawerListView?.let {
            it?.adapter = object : ArrayAdapter<String>(activity,
                    android.R.layout.simple_list_item_activated_1,
                    android.R.id.text1,
                    arrayOf(getString(R.string.title_section1),
                            getString(R.string.title_section2),
                            getString(R.string.title_section3),
                            getString(R.string.title_section4),
                            getString(R.string.title_section5))) {}
            it.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                selectItem(position)
            }
            it.setItemChecked(mCurrentSelectedPosition, true)
        }

        return mDrawerListView;

        /*
        return container?.let {
            with(it) {
                mDrawerListView = listView {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    choiceMode = ListView.CHOICE_MODE_SINGLE
                    divider = ColorDrawable(resources.getColor(android.R.color.transparent))
                    dividerHeight = 0
                    background = ColorDrawable(Color.parseColor("#333"))
                    onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        selectItem(position)
                    }
                    setItemChecked(mCurrentSelectedPosition, true)
                    adapter = object : ArrayAdapter<String>(activity,
                            android.R.layout.simple_list_item_activated_1,
                            android.R.id.text1,
                            arrayOf(getString(R.string.title_section1),
                                    getString(R.string.title_section2),
                                    getString(R.string.title_section3),
                                    getString(R.string.title_section4),
                                    getString(R.string.title_section5))) {}

                }
                mDrawerListView
            }
        }*/
    }

    fun isDrawerOpen(): Boolean {
        mDrawerLayout?.let {
            mFragmentContainerView?.let {
                return mDrawerLayout?.isDrawerOpen(it) ?: false
            }
        }
        return false
    }

    fun setUp(fragmentId: Int, drawerLayout: DrawerLayout) {
        activity?.let {
            mFragmentContainerView = it.findViewById(fragmentId)
            mDrawerLayout = drawerLayout
            mDrawerLayout?.setDrawerShadow(R.drawable.abc_ic_star_half_black_16dp, GravityCompat.START)

            mDrawerToggle = object : ActionBarDrawerToggle(it, mDrawerLayout,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close) {

                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView)
                    if (isAdded) {
                        it.invalidateOptionsMenu()
                    }
                }
                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    if(isAdded) {
                        if (!mUserLearnedDrawer) {
                            mUserLearnedDrawer = true
                            PreferenceManager.getDefaultSharedPreferences(activity).apply {
                                edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply()
                            }
                        }
                        it.invalidateOptionsMenu()
                    }
                }
            }

            if(!mUserLearnedDrawer && !mFromSavedInstanceState) {
                mDrawerLayout?.openDrawer(mFragmentContainerView)
            }
            mDrawerLayout?.post {
                mDrawerToggle.syncState()
            }
            mDrawerLayout?.addDrawerListener(mDrawerToggle)
        }
    }

    private fun selectItem(position: Int) {
        mCurrentSelectedPosition = position
        mDrawerListView?.let {
            it.setItemChecked(position, true)
        }
        mDrawerLayout?.let {
            it.closeDrawer(mFragmentContainerView)
        }
        mCallbacks?.let {
            it.onNavigationDrawerItemSelected(position)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mCallbacks = activity as NavigationDrawerCallbacks
        } catch (e: ClassCastException) {
            throw ClassCastException("Activity must implement NavigationDrawerCallbacks.")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallbacks = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (mDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)

    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        fun onNavigationDrawerItemSelected(position: Int)
    }
}