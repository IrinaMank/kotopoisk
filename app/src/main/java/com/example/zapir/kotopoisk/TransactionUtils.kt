package com.example.zapir.kotopoisk

import android.support.annotation.BoolRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.zapir.kotopoisk.ui.fragment.BaseFragment
import org.slf4j.LoggerFactory

class TransactionUtils {
    companion object {

        /*fun getCurrentFragment(fragmentManager: FragmentManager): Fragment? {
            val topFragmentPosition = fragmentManager.backStackEntryCount - 1
            val entry = fragmentManager.getBackStackEntryAt(topFragmentPosition)
            return fragmentManager.findFragmentByTag(entry.name)
        }*/

        fun getCurrentFragment(fragmentManager: FragmentManager): Fragment? {
            val topFragmentPosition = fragmentManager.backStackEntryCount - 1
            val entry = fragmentManager.getBackStackEntryAt(topFragmentPosition)
            return if (entry != null) fragmentManager.findFragmentByTag(entry.name) else null
        }

        fun addFragment(manager: FragmentManager, containerId: Int, fragment: BaseFragment) {
            fragment.logger.info("addFragment")
            manager.beginTransaction()
                    .add(fragment, containerId.toString())
                    .addToBackStack(fragment.javaClass.name)
                    .commit()
        }

        fun replaceFragment(manager: FragmentManager, containerId: Int, fragment: BaseFragment) {
            fragment.logger.info("replaceFragment")
            manager.beginTransaction()
                    .replace(containerId, fragment)
                    .addToBackStack(fragment.javaClass.name)
                    .commit()
        }

        fun isEmpty(fragmentManager: FragmentManager): Boolean {
            return fragmentManager.backStackEntryCount == 0
        }

    }
}