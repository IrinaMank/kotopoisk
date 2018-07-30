package com.example.zapir.kotopoisk.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.TransactionUtils
import com.example.zapir.kotopoisk.firestoreApi.ticket.TicketFirestoreController
import com.example.zapir.kotopoisk.model.Ticket
import com.example.zapir.kotopoisk.ui.fragment.BaseFragment
import com.example.zapir.kotopoisk.ui.ticket.TicketDetailFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment() {

    companion object {

        fun newInstance(): SearchFragment = SearchFragment()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_text.setOnClickListener { handlerClickSearchText() }

    }

    private fun handlerClickSearchText() {
        (parentFragment as BaseFragment).replaceFragment(TicketDetailFragment.newInstance())
    }

}