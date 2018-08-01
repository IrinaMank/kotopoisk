package com.example.zapir.kotopoisk.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.domain.bottomBarApi.TransactionUtils
import com.example.zapir.kotopoisk.ui.base.BaseFragment

class SearchFragmentHolder : BaseFragment() {

    companion object {

        fun newInstance() = SearchFragmentHolder()

    }

    override fun replaceFragment(fragment: BaseFragment) {
        TransactionUtils.replaceFragment(childFragmentManager, R.id.container, fragment)
    }

    override fun addFragment(fragment: BaseFragment) {
        TransactionUtils.addFragment(childFragmentManager, R.id.container, fragment)
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
            replaceFragment(SearchFragment.newInstance())
        }
    }

}