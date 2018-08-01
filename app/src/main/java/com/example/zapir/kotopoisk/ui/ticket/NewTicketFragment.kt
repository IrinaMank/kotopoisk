package com.example.zapir.kotopoisk.ui.ticket

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_new_ticket.*
import android.widget.ArrayAdapter


class NewTicketFragment : BaseFragment() {

    companion object {
        const val TAG = "creating new ticket"
        private const val INSTANCE_MESSAGE_KEY = "arguments for NewTicketFragment"

        fun newInstance(ticket: Ticket): NewTicketFragment {
            return NewTicketFragment().apply {
                val arguments = Bundle()
                arguments.putParcelable(INSTANCE_MESSAGE_KEY, ticket)
                setArguments(arguments)
                Log.d("sent instance of", "AddingPhotoFragment")
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newTicket = arguments?.getParcelable<Ticket>(INSTANCE_MESSAGE_KEY)
                ?: throw Exception("no Ticket in NewTicketFragment arguments")

        new_ticket_photo.setImageURI(Uri.parse(newTicket.photo.url))
        val type =
                when (newTicket.type) {
                    0 -> R.id.cat_radio
                    else -> R.id.dog_radio
                }
        radio_type.check(type)

        val breedAdapter =
                when (newTicket.type) {
                    0 -> ArrayAdapter.createFromResource(activity, R.array.cat_breed_array, android.R.layout.simple_spinner_item)
                    else -> ArrayAdapter.createFromResource(activity, R.array.dog_breed_array, android.R.layout.simple_spinner_item)
                }
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_breed.adapter = breedAdapter
        spinner_breed.prompt = getString(R.string.breed)

        spinner_color.prompt = getString(R.string.color)

        val sizeAdapter =
                when (newTicket.type) {
                    0 -> ArrayAdapter.createFromResource(activity, R.array.cat_size, android.R.layout.simple_spinner_item)
                    else -> ArrayAdapter.createFromResource(activity, R.array.dog_size, android.R.layout.simple_spinner_item)
                }
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_size.adapter = sizeAdapter
        spinner_breed.prompt = getString(R.string.size)

        cat_radio.setOnClickListener { changeSpinnersForCat() }
        dog_radio.setOnClickListener { changeSpinnersForDog() }
    }

    private fun changeSpinnersForCat(){
        val breedAdapter = ArrayAdapter.createFromResource(activity,
                R.array.cat_breed_array, android.R.layout.simple_spinner_item)
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_breed.adapter = breedAdapter

        val sizeAdapter = ArrayAdapter.createFromResource(activity,
                R.array.cat_size, android.R.layout.simple_spinner_item)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_size.adapter = sizeAdapter
    }

    private fun changeSpinnersForDog(){
        val breedAdapter = ArrayAdapter.createFromResource(activity,
                R.array.dog_breed_array, android.R.layout.simple_spinner_item)
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_breed.adapter = breedAdapter

        val sizeAdapter = ArrayAdapter.createFromResource(activity,
                R.array.dog_size, android.R.layout.simple_spinner_item)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_size.adapter = sizeAdapter
    }

}