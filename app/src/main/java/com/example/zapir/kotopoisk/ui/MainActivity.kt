package com.example.zapir.kotopoisk.ui

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.view.MenuItem
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.common.SelectedPage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private var toolbar: ActionBar? = null
    private var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomNavigationViewHelper()
    }

    override fun selectBottomBarTab(position: Int) {
        logger.info("Navigation: ${navigation_view.menu.getItem(position).title}")
        pager.currentItem = position
        toolbar?.title = navigation_view.menu.getItem(position).title
    }

    private fun initBottomNavigationViewHelper() {
        // Set toolbar
        toolbar = supportActionBar

        // Set BottomBarPagerAdapter
        pager.adapter = BottomBarPagerAdapter(supportFragmentManager)

        // Set listener item selected
        navigation_view.setOnNavigationItemSelectedListener {
            handlerNavigationListener(it)
        }

        // Set listener change item
        pager.addOnPageChangeListener(handlerPageChangeListener)

        pager.offscreenPageLimit = SelectedPage.values().size

        // Set view on center fragment
        selectBottomBarTab(SelectedPage.MAP.value)
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        val currentHostFragment = TransactionUtils.getCurrentFragment(supportFragmentManager)

        if (currentHostFragment != null) {
            val childManager = currentHostFragment.childFragmentManager
            if (childManager.backStackEntryCount > 1) {
                childManager.popBackStack()
                return
            }
        }

        finish()
    }*/

    private fun handlerNavigationListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_search -> selectBottomBarTab(SelectedPage.SEARCH.value)
            R.id.navigation_map -> selectBottomBarTab(SelectedPage.MAP.value)
            R.id.navigation_profile -> selectBottomBarTab(SelectedPage.PROFILE.value)
            else -> return false
        }

        return true
    }

    private val handlerPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageSelected(position: Int) {
            prevMenuItem?.isChecked = false
            navigation_view.menu.getItem(position).isChecked = true
            selectBottomBarTab(position)
        }

    }

}