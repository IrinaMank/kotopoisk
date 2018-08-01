package com.example.zapir.kotopoisk.ui.search

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.ui.base.BaseFragment
import com.example.zapir.kotopoisk.ui.map.LoadListener
import com.example.zapir.kotopoisk.ui.ticket.OverviewTicketFragment
import com.example.zapir.kotopoisk.ui.tickets_recycler.OnItemClickListener
import com.example.zapir.kotopoisk.ui.tickets_recycler.TicketAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_my_ticket_list.*

class SearchTicketsList : BaseFragment(), OnItemClickListener, LoadListener {

    companion object {
        private const val INSTANCE_MESSAGE_KEY = "arguments for SearchTicketsList"
        private const val SAVED_LIST = "Saved list"

        fun newInstance(ticket: Ticket): SearchTicketsList {
            return SearchTicketsList().apply {
                val arguments = Bundle()
                arguments.putParcelable(INSTANCE_MESSAGE_KEY, ticket)
                setArguments(arguments)
            }
        }

    }

    private val adapter = TicketAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_ticket_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ticket = arguments?.getParcelable<Ticket>(INSTANCE_MESSAGE_KEY)
                ?: throw RuntimeException("SearchTicketsList has no ticket in arguments")
        configRecycler(ticket, savedInstanceState)
    }

    private fun configRecycler(ticket: Ticket, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            adapter.items = savedInstanceState.getParcelableArrayList(SAVED_LIST)
        } else {
            setLoadStart()
            ticketController.searchTicket(ticket)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                setLoadGone()
                                adapter.items = ArrayList(it)
                                if (it.isEmpty()) {
                                    my_tickets_placeholder.visibility = View.VISIBLE
                                    showToast(getBaseActivity(), "По вашему запросу ничего не найдено.")
                                }
                            },
                            {
                                errorHandler.handleException(it, context!!)
                            }
                    )
        }

        my_tickets_recycler.layoutManager = LinearLayoutManager(activity)
        my_tickets_recycler.addItemDecoration(
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
        my_tickets_recycler.adapter = adapter

//        val helper = ItemTouchHelper(SwipeCallback(
//                { adapter.removeAt(it) }
//        ))
//        helper.attachToRecyclerView(search_tickets_recycler)
    }

    override fun onItemClick(ticket: Ticket) {
        (parentFragment as BaseFragment).addFragment(OverviewTicketFragment.newInstance(ticket))
    }

    override fun onFavorClick(ticket: Ticket) {
        if (ticket.isFavorite) {
            ticketController.makeTicketUnFavourite(ticket)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {

                            },
                            {
                                errorHandler.handleException(it, context!!)
                            }
                    )
        } else {
            ticketController.makeTicketFavourite(ticket)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {

                            },
                            {
                                errorHandler.handleException(it, context!!)
                            }
                    )
        }
    }

    override fun setLoadStart() {
        my_tickets_progress_bar.visibility = View.VISIBLE
        val animation = my_tickets_progress_bar.background as AnimationDrawable
        animation.start()
    }

    override fun setLoadGone() {
        my_tickets_progress_bar.visibility = View.GONE
    }

}