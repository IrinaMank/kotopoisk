package com.zapir.kotopoisk.ui.search

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.model.Ticket
import com.zapir.kotopoisk.ui.base.BaseFragment
import com.zapir.kotopoisk.ui.map.LoadListener
import com.zapir.kotopoisk.ui.ticket.OverviewTicketFragment
import com.zapir.kotopoisk.ui.tickets_recycler.OnItemClickListener
import com.zapir.kotopoisk.ui.tickets_recycler.TicketAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_my_ticket_list.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit

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
        back_button.visibility = View.VISIBLE
        back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        toolbar_title.text = getString(R.string.search_results)
        configRecycler(ticket, savedInstanceState)
    }

    private fun configRecycler(ticket: Ticket, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            adapter.items = savedInstanceState.getParcelableArrayList(SAVED_LIST)
        } else {
            setLoadStart()
            ticketController.searchTicket(ticket)
                    .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                adapter.clearItems()
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
        (parentFragment as BaseFragment).replaceFragment(OverviewTicketFragment.newInstance(ticket))
    }

    override fun onFavorClick(ticket: Ticket) {
        if (ticket.isFavorite) {
            //ticket.isFavorite = false
            ticketController.makeTicketUnFavourite(ticket)
                    .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                //ticket.isFavorite = false
                            },
                            {
                                errorHandler.handleException(it, context!!)
                            }
                    )
        } else {
            //ticket.isFavorite = true
            ticketController.makeTicketFavourite(ticket)
                    .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                //ticket.isFavorite = true
                            },
                            {
                                errorHandler.handleException(it, context!!)
                            }
                    )
        }
    }

    override fun setLoadStart() {
        my_tickets_progress_bar?.visibility = View.VISIBLE
        my_tickets_recycler?.visibility = View.GONE
        val animation = my_tickets_progress_bar.background as AnimationDrawable
        animation.start()
    }

    override fun setLoadGone() {
        my_tickets_progress_bar?.visibility = View.GONE
        my_tickets_recycler?.visibility = View.VISIBLE
    }

}