package com.example.zapir.kotopoisk

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.zapir.kotopoisk.fragment.MapFragment
import com.example.zapir.kotopoisk.viewHolder.MapFragmentHolder
import com.example.zapir.kotopoisk.viewHolder.ProfileFragmentHolder
import com.example.zapir.kotopoisk.viewHolder.SearchFragmentHolder

class CustomPagerAdapter(mgr: FragmentManager) : FragmentPagerAdapter(mgr) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SearchFragmentHolder.newInstance()
            1 -> MapFragmentHolder.newInstance()
            2 -> ProfileFragmentHolder.newInstance()
            else -> Fragment()
        }
    }

    override fun getCount(): Int = 3

}