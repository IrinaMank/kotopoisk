package com.zapir.kotopoisk.ui.profile

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.model.Ticket
import com.zapir.kotopoisk.data.model.User
import com.zapir.kotopoisk.ui.base.BaseFragment
import com.zapir.kotopoisk.ui.map.LoadListener
import com.zapir.kotopoisk.ui.ticket.OverviewTicketFragment
import com.zapir.kotopoisk.ui.tickets_recycler.OnItemClickListener
import com.zapir.kotopoisk.ui.tickets_recycler.TicketAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_my_ticket_list.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit

class FavoriteTicketsFragment : BaseFragment(), OnItemClickListener, LoadListener {

    companion object {

        private const val ARG_USER = "Arg_user"
        private const val SAVED_LIST = "Favorite_tickets_saved_list"

        fun newInstance(user: User): FavoriteTicketsFragment = FavoriteTicketsFragment().apply {
            val arguments = Bundle()
            arguments.putParcelable(FavoriteTicketsFragment.ARG_USER, user)
            this.arguments = arguments
        }

    }


    private val adapter = TicketAdapter(this)
    val user: User by lazy {
        arguments?.getParcelable(FavoriteTicketsFragment.ARG_USER) as? User ?: throw
        RuntimeException("No user in arguments")
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_ticket_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoadStart()
        back_button.visibility = View.VISIBLE
        back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        toolbar_title.text = getString(R.string.favorite_tickets)
        setRecycler(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_LIST, adapter.items)
    }


    private fun setRecycler(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            adapter.items = savedInstanceState.getParcelableArrayList(SAVED_LIST)
        } else {
            disposables.add(
                    ticketController.getFavouriteTickets(user.id)
                            .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        adapter.clearItems()
                                        adapter.items = ArrayList(it)
                                        if (it.isEmpty()) {
                                            my_tickets_placeholder.visibility = View.VISIBLE
                                        }
                                        setLoadGone()
                                    },
                                    {
                                        errorHandler.handleException(it, context!!)
                                    }
                            )
            )
        }
        my_tickets_recycler.layoutManager = LinearLayoutManager(activity)
        my_tickets_recycler.adapter = adapter
    }

    override fun onItemClick(ticket: Ticket) {
        (parentFragment as BaseFragment).replaceFragment(OverviewTicketFragment.newInstance(ticket))
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

    override fun onFavorClick(ticket: Ticket) {
        if (ticket.isFavorite) {
            ticketController.makeTicketUnFavourite(ticket)
                    .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
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
                    .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
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
}
