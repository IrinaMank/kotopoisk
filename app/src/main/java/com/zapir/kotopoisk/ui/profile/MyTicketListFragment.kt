package com.zapir.kotopoisk.ui.profile

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.model.Ticket
import com.zapir.kotopoisk.data.model.User
import com.zapir.kotopoisk.domain.bottomBarApi.TransactionUtils
import com.zapir.kotopoisk.ui.base.BaseActivity
import com.zapir.kotopoisk.ui.base.BaseFragment
import com.zapir.kotopoisk.ui.map.LoadListener
import com.zapir.kotopoisk.ui.ticket.OverviewTicketFragment
import com.zapir.kotopoisk.ui.tickets_recycler.OnItemClickListener
import com.zapir.kotopoisk.ui.tickets_recycler.SwipeCallback
import com.zapir.kotopoisk.ui.tickets_recycler.TicketAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_my_ticket_list.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit

class MyTicketListFragment : BaseFragment(), OnItemClickListener, LoadListener {

    companion object {

        private const val ARG_USER = "Arg_user"
        private const val SAVED_LIST = "My_tickets_saved_list"

        fun newInstance(user: User): MyTicketListFragment = MyTicketListFragment().apply {
            val arguments = Bundle()
            arguments.putParcelable(MyTicketListFragment.ARG_USER, user)
            this.arguments = arguments
        }

    }

    private val adapter = TicketAdapter(this)
    val user: User by lazy {
        arguments?.getParcelable(MyTicketListFragment.ARG_USER) as? User ?: throw
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
        setRecycler(savedInstanceState)
        back_button.visibility = View.VISIBLE
        back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        toolbar_title.text = getString(R.string.my_tickets)
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
            setLoadStart()
            disposables.add(
                    ticketController.getUserTickets(user.id)
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

        val helper = ItemTouchHelper(SwipeCallback({ onSwipe(it) }))
        helper.attachToRecyclerView(my_tickets_recycler)

    }

    override fun onItemClick(ticket: Ticket) {
        (parentFragment as BaseFragment).replaceFragment(OverviewTicketFragment.newInstance(ticket))
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


    private val onSwipe = { position: Int ->
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.delete_ticket))
        builder.setMessage(getString(R.string.sure_delete_ticket))
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            ticketController.deleteTicket(adapter.items[position])
                    .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                user.petCount--
                            },
                            {
                                errorHandler.handleException(it, context!!)
                            }
                    )
            adapter.removeAt(position)
        }

        builder.setNegativeButton(getString(R.string.cancel)) { _, _ ->
            adapter.notifyItemChanged(position)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
