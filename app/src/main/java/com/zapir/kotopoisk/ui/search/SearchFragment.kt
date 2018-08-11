package com.zapir.kotopoisk.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.model.Ticket
import com.zapir.kotopoisk.domain.common.TypesConverter
import com.zapir.kotopoisk.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_ticket_search.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : BaseFragment() {

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ticket_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = radio_type.checkedRadioButtonId

        val breedAdapter =
                when (type) {
                    R.id.cat_radio -> ArrayAdapter.createFromResource(activity, R.array.cat_breed_array, android.R.layout.simple_spinner_item)
                    else -> ArrayAdapter.createFromResource(activity, R.array.dog_breed_array, android.R.layout.simple_spinner_item)
                }
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_breed.adapter = breedAdapter

        val sizeAdapter =
                when (type) {
                    R.id.cat_radio -> ArrayAdapter.createFromResource(activity, R.array.cat_size, android.R.layout.simple_spinner_item)
                    else -> ArrayAdapter.createFromResource(activity, R.array.dog_size, android.R.layout.simple_spinner_item)
                }
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_size.adapter = sizeAdapter

        cat_radio.setOnClickListener { changeSpinnersForCat() }
        dog_radio.setOnClickListener { changeSpinnersForDog() }
        search_button.setOnClickListener { startSearch() }
        toolbar_title.text = getString(R.string.toolbar_string_search)
    }

    private fun changeSpinnersForCat() {
        val breedAdapter = ArrayAdapter.createFromResource(activity,
                R.array.cat_breed_array, android.R.layout.simple_spinner_item)
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_breed.adapter = breedAdapter

        val sizeAdapter = ArrayAdapter.createFromResource(activity,
                R.array.cat_size, android.R.layout.simple_spinner_item)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_size.adapter = sizeAdapter
    }

    private fun changeSpinnersForDog() {
        val breedAdapter = ArrayAdapter.createFromResource(activity,
                R.array.dog_breed_array, android.R.layout.simple_spinner_item)
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_breed.adapter = breedAdapter

        val sizeAdapter = ArrayAdapter.createFromResource(activity,
                R.array.dog_size, android.R.layout.simple_spinner_item)
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_size.adapter = sizeAdapter
    }

    private fun startSearch() {
        val ticket = Ticket(
                date = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.US).format(Date())
        )

        ticket.type =
                when (radio_type.checkedRadioButtonId) {
                    R.id.cat_radio -> TypesConverter.getTypeFromString(getString(R.string.cat), getBaseActivity())
                    else -> TypesConverter.getTypeFromString(getString(R.string.dog), getBaseActivity())
                }
        ticket.breed = TypesConverter.getBreedFromString(spinner_breed.selectedItem.toString(),
                ticket.type, getBaseActivity())
        ticket.color = TypesConverter.getColorFromString(spinner_color.selectedItem.toString(), getBaseActivity())
        ticket.furLength = TypesConverter.getFurLengthFromString(spinner_furLength.selectedItem.toString(), getBaseActivity())
        ticket.hasCollar = collar_switch_compat.isChecked

        handlerClickSearch(ticket)

    }

    private fun handlerClickSearch(ticket: Ticket) {
        (parentFragment as BaseFragment).replaceFragment(SearchTicketsList.newInstance(ticket))
    }

}