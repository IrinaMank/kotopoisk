package com.zapir.kotopoisk.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.domain.bottomBarApi.TransactionUtils
import com.zapir.kotopoisk.ui.base.BaseFragment

class MapFragmentHolder : BaseFragment() {

    companion object {

        fun newInstance() = MapFragmentHolder()

    }

    override fun replaceFragment(fragment: BaseFragment) {
        TransactionUtils.replaceFragment(childFragmentManager, R.id.container, fragment)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_holder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            TransactionUtils.replaceFragment(childFragmentManager, R.id.container, MapFragment.newInstance())
        }
    }

}