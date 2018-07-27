package com.example.zapir.kotopoisk

import android.annotation.SuppressLint
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.view.MenuItem
import org.slf4j.LoggerFactory

class BottomNavigationViewHelper private constructor() {

    val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    var toolbar: ActionBar? = null
    lateinit var navigationView: BottomNavigationView
    lateinit var viewPager: ViewPager
    lateinit var fragmentManager: FragmentManager

    private var prevMenuItem: MenuItem? = null
    private var currentPosition: Int = 1

    companion object {

        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: BottomNavigationViewHelper

        fun instance(): BottomNavigationViewHelper {
            return instance
        }

        fun create(
                toolbar: ActionBar?,
                navigationView: BottomNavigationView,
                viewPager: ViewPager,
                fragmentManager: FragmentManager
        ) {
            instance = BottomNavigationViewHelper().apply {
                this.toolbar = toolbar
                this.navigationView = navigationView
                this.viewPager = viewPager
                this.fragmentManager = fragmentManager

                // Set current fragment on ViewPager
                this.viewPager.currentItem = this.currentPosition

                // Set CustomPagerAdapter
                this.viewPager.adapter = CustomPagerAdapter(fragmentManager)

                // Set listener item selected
                navigationView.setOnNavigationItemSelectedListener {
                    handlerNavigationListener(it)
                }

                // Set listener change item
                viewPager.addOnPageChangeListener(handlerPageChangeListener)

                // Set view on center fragment
                viewPager.currentItem = 1
                toolbar?.title = navigationView.menu.getItem(1).title
                navigationView.menu.getItem(1).isChecked = true
            }
        }

    }

    private fun handlerNavigationListener(item: MenuItem): Boolean {
        // ООП застрелилось в этом куске кода; свич-кейс - зло
        return when (item.itemId) {
            R.id.navigation_search -> {
                logger.info("Navigation: search")
                viewPager.currentItem = 0
                toolbar?.title = item.title
                true
            }

            R.id.navigation_map -> {
                logger.info("Navigation: map")
                viewPager.currentItem = 1
                toolbar?.title = item.title
                true
            }

            R.id.navigation_profile -> {
                logger.info("Navigation: profile")
                viewPager.currentItem = 2
                toolbar?.title = item.title
                return true
            }

            else -> false
        }
    }

    private val handlerPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            if (prevMenuItem != null) {
                prevMenuItem?.isChecked = false
            } else {
                navigationView.menu.getItem(0).isChecked = false
            }

            navigationView.menu.getItem(position).isChecked = true
            toolbar?.title = navigationView.menu.getItem(position).title
            prevMenuItem = navigationView.menu.getItem(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
        }
    }

}