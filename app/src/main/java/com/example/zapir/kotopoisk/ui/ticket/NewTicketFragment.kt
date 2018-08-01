package com.example.zapir.kotopoisk.ui.ticket

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_new_ticket.*
import android.widget.ArrayAdapter
import com.example.zapir.kotopoisk.data.model.User
import com.example.zapir.kotopoisk.domain.common.SelectedPage
import com.example.zapir.kotopoisk.domain.common.TypesConverter
import com.example.zapir.kotopoisk.ui.login.LoginActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*


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

    private var ticket = Ticket(
            date = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.US).format(Date())
    )

    private val watcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            updateTicket()
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
        ticketController.uploadPhoto(Uri.parse(newTicket.photo.url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            newTicket.photo.url = it
                        },
                        { errorHandler.handleException(it, getBaseActivity()) }
                )
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

        cat_radio.setOnClickListener {
            ticket.type = 0
            updateTicket()
            changeSpinnersForCat()
        }
        dog_radio.setOnClickListener {
            ticket.type = 1
            updateTicket()
            changeSpinnersForDog()
        }

        new_ticket_save_button.setOnClickListener { updateTicket() }

        new_ticket_publish_button.setOnClickListener { publishTicket() }
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

    private fun initTicket(){
        val userId = preferencesManager.getString(LoginActivity.PREFS_ID)
        ticket.user.id = userId
        ticket.photo.userId = userId
        ticket.photo.ticketId = ticket.id
        ticket.type =
                when(radio_type.checkedRadioButtonId){
                    R.id.cat_radio -> TypesConverter.getTypeFromString(getString(R.string.cat), getBaseActivity())
                    else -> TypesConverter.getTypeFromString(getString(R.string.dog), getBaseActivity())
                }
        ticket.breed = TypesConverter.getBreedFromString(spinner_breed.selectedItem.toString(),
                ticket.type, getBaseActivity())
        ticket.color = TypesConverter.getColorFromString(spinner_color.selectedItem.toString(), getBaseActivity())
        ticket.furLength = TypesConverter.getFurLengthFromString(spinner_furLength.selectedItem.toString(), getBaseActivity())
        ticket.hasCollar = collar_switch_compat.isChecked
        ticket.overview = description.text.toString()
    }

    private fun publishTicket(){
        initTicket()
        ticket.isPublished = true
        ticketController.publishTicket(ticket)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { showToast(getBaseActivity(), "ticket published") },
                        { errorHandler.handleException(it, getBaseActivity()) }
                )
    }

    private fun updateTicket(){
        initTicket()
        ticketController.updateTicket(ticket)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            getBaseActivity().selectBottomBarTab(SelectedPage.MAP.value)
                        },
                        { errorHandler.handleException(it, getBaseActivity()) }
                )
    }

}