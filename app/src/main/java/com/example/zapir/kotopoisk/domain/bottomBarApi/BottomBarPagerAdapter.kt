package com.example.zapir.kotopoisk.domain.bottomBarApi

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.example.zapir.kotopoisk.ui.map.MapFragmentHolder
import com.example.zapir.kotopoisk.ui.profile.ProfileFragmentHolder
import com.example.zapir.kotopoisk.ui.search.SearchFragmentHolder

class BottomBarPagerAdapter(mgr: FragmentManager) : FragmentPagerAdapter(mgr) {

    private val PAGE_COUNT = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SearchFragmentHolder.newInstance()
            1 -> MapFragmentHolder.newInstance()
            2 -> ProfileFragmentHolder.newInstance()
            else -> Fragment()
        }
    }

    override fun getCount(): Int = PAGE_COUNT

}