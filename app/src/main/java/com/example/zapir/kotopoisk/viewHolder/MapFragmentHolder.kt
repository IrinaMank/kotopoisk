package com.example.zapir.kotopoisk.viewHolder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.TransactionUtils
import com.example.zapir.kotopoisk.fragment.BaseFragment
import com.example.zapir.kotopoisk.fragment.MapFragment

class MapFragmentHolder : BaseFragment() {

    companion object {

        fun newInstance() = MapFragment.newInstance()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        if (savedInstanceState == null) {
            TransactionUtils.addFragment(MapFragment.newInstance(), R.id.container)
        }

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

}