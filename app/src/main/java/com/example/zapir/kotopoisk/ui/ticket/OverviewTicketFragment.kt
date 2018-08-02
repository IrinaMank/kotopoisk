package com.example.zapir.kotopoisk.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.domain.common.TypesConverter
import com.example.zapir.kotopoisk.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_ticket_overview.*
import kotlinx.android.synthetic.main.toolbar.*

class OverviewTicketFragment : BaseFragment() {

    companion object {
        private const val INSTANCE_MESSAGE_KEY = "arguments for OverviewTicketFragment"

        fun newInstance(ticket: Ticket): BaseFragment {
            return OverviewTicketFragment().apply {
                val arguments = Bundle()
                arguments.putParcelable(INSTANCE_MESSAGE_KEY, ticket)
                setArguments(arguments)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_button.visibility = View.VISIBLE
        back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        toolbar_title.text = getString(R.string.ticket_overview)

        val ticket = arguments?.getParcelable<Ticket>(INSTANCE_MESSAGE_KEY)
                ?: throw RuntimeException("OverviewTicketFragment has no ticket in arguments")

        Glide.with(view)
                .load(ticket.photo.url)
                .into(overview_ticket_photo)
        animal_type.text = TypesConverter.getStringFromType(ticket.type, getBaseActivity())
        breed.text = TypesConverter.getStringFromBreed(ticket.breed
                ?: throw RuntimeException("ticket ${ticket.id} has no breed"),
                ticket.type, getBaseActivity())
        color.text = TypesConverter.getStringFromColor(ticket.color, getBaseActivity())
        size.text = TypesConverter.getStringFromSize(ticket.size, ticket.type, getBaseActivity())
        furLength.text = TypesConverter.getStringFromFurLength(ticket.furLength, getBaseActivity())
        collar_switch_compat.apply {
            isClickable = false
            isChecked = ticket.hasCollar
        }
        description.text = ticket.overview

        overview_go_button.setOnClickListener { }
    }

}