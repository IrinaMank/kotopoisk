package com.example.zapir.kotopoisk.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.ui.fragment.BaseFragment

class TicketListFragment : BaseFragment() {

    companion object {

        fun newInstance(): TicketListFragment = TicketListFragment()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ticket_list, container, false)
    }

}