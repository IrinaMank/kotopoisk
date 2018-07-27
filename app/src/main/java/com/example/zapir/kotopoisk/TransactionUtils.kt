package com.example.zapir.kotopoisk

import android.app.Fragment
import android.app.FragmentManager
import com.example.zapir.kotopoisk.fragment.BaseFragment

class TransactionUtils {

    companion object {

        fun getCurrentFragment(fragmentManager: FragmentManager): Fragment {
            val topFragmentPosition = fragmentManager.backStackEntryCount - 1
            val entry = fragmentManager.getBackStackEntryAt(topFragmentPosition)
            return fragmentManager.findFragmentByTag(entry.name)
        }

        fun addFragment(fragment: BaseFragment, containerId: Int) {
            //TODO("Узнать, что эта функция должна делать")
        }

    }

}