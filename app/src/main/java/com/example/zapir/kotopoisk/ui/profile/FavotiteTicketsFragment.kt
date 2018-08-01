package com.example.zapir.kotopoisk.ui.profile

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.User
import com.example.zapir.kotopoisk.ui.base.BaseFragment
import com.example.zapir.kotopoisk.ui.tickets_recycler.SwipeCallback
import com.example.zapir.kotopoisk.ui.tickets_recycler.TicketAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_my_ticket_list.*

class FavotiteTicketsFragment : BaseFragment() {

    companion object {

        private const val ARG_USER = "Arg_user"
        private const val SAVED_LIST = "Favorite_tickets_saved_list"

        fun newInstance(user: User): FavotiteTicketsFragment = FavotiteTicketsFragment().apply {
            val arguments = Bundle()
            arguments.putParcelable(FavotiteTicketsFragment.ARG_USER, user)
            this.arguments = arguments
        }

    }


    //private val adapter = TicketAdapter()
    val user: User by lazy {
        arguments?.getParcelable(FavotiteTicketsFragment.ARG_USER) as? User ?: throw
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
        setHasOptionsMenu(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putParcelableArrayList(SAVED_LIST, adapter.items)
    }


    private fun setRecycler(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            //adapter.items = savedInstanceState.getParcelableArrayList(SAVED_LIST)
        } else {
            disposables.add(
                    ticketController.getFavouriteTickets(user.id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                       // adapter.items = ArrayList(it)
                                        if (it.isEmpty()) {
                                            my_tickets_placeholder.visibility = View.VISIBLE
                                        }
                                    },
                                    {
                                        errorHandler.handleException(it, context!!)
                                    }
                            )
            )
        }
        my_tickets_recycler.layoutManager = LinearLayoutManager(activity)
        //my_tickets_recycler.adapter = adapter

        //val helper = ItemTouchHelper(SwipeCallback({ adapter.removeAt(it) }))
        //helper.attachToRecyclerView(my_tickets_recycler)
    }
}