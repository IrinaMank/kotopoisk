package com.example.zapir.kotopoisk.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.common.SelectedPage
import com.example.zapir.kotopoisk.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_ticket_detail.*

class TicketDetailFragment : BaseFragment() {

    companion object {

        fun newInstance(): TicketDetailFragment = TicketDetailFragment()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ticket_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ticket_detail_text.setOnClickListener { handlerClickSearchText() }
    }

    private fun handlerClickSearchText() {
        getBaseActivity().selectBottomBarTab(SelectedPage.MAP.value)
    }

}