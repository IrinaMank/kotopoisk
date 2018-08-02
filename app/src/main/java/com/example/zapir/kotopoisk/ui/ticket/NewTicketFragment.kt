package com.example.zapir.kotopoisk.ui.ticket

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.zapir.kotopoisk.KotopoiskApplication
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.data.model.User
import com.example.zapir.kotopoisk.domain.common.SelectedPage
import com.example.zapir.kotopoisk.domain.common.TypesConverter
import com.example.zapir.kotopoisk.ui.base.BaseFragment
import com.example.zapir.kotopoisk.ui.login.LoginActivity
import com.example.zapir.kotopoisk.ui.main.MainActivity
import com.example.zapir.kotopoisk.ui.map.LocationActivity
import com.fernandocejas.arrow.optional.Optional
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_new_ticket.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*


class NewTicketFragment : BaseFragment() {

    companion object {
        private const val INSTANCE_MESSAGE_KEY = "arguments for NewTicketFragment"
        private const val GET_LOCATION_CODE = 1
        const val GET_LOCATION_KEY = "Please, activity, get position for this pretty pet"

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newTicket = arguments?.getParcelable<Ticket>(INSTANCE_MESSAGE_KEY)
                ?: throw Exception("no Ticket in NewTicketFragment arguments")

        back_button.visibility = View.VISIBLE
        back_button.setOnClickListener {
            activity?.onBackPressed()
        }

        toolbar_title.text = getString(R.string.new_ticket)

        new_ticket_photo.setImageURI(Uri.parse(newTicket.photo.url))
        ticketController.uploadPhoto(Uri.parse(newTicket.photo.url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            ticket.photo.url = it
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
            updateTicket(false)
            changeSpinnersForCat()
        }
        dog_radio.setOnClickListener {
            ticket.type = 1
            updateTicket(false)
            changeSpinnersForDog()
        }

        new_ticket_save_button.setOnClickListener {
            if(it.isActivated){
                updateTicket(true)
            } else {
                showToast(getBaseActivity(), "Установите место обнаружения")
            }
        }
        new_ticket_publish_button.setOnClickListener {
            if(it.isActivated){
                publishTicket()
            } else {
                showToast(getBaseActivity(), "Установите место обнаружения")
            }
        }
        location.setOnClickListener { handlerClickerListener() }
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

    private fun initTicket(): Single<Optional<User>> {
        val userId = preferencesManager.getString(LoginActivity.PREFS_ID)
        ticket.user.id = userId
        ticket.photo.userId = userId
        ticket.photo.ticketId = ticket.id
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
        ticket.overview = description.text.toString()
        return userController.getUser(userId)
    }

    private fun publishTicket() {
        if (location.text != getString(R.string.location_map)) {
            showToast(getBaseActivity(), getString(R.string.error_location))
            return
        }


        initTicket().observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            ticket.isPublished = true
                            ticket.user = it.get()
                            ticketController.publishTicket(ticket)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            {
                                                KotopoiskApplication.rxBus().postNewTicket(ticket)
                                                navigateToMap()
                                                //showToast(getBaseActivity(), "ticket published")
                                            },
                                            {
                                                errorHandler.handleException(it, getBaseActivity())
                                            }
                                    )
                        },
                        {
                            errorHandler.handleException(it, getBaseActivity())
                        }
                )

    }

    private fun updateTicket(finish: Boolean) {
        initTicket()
        ticketController.updateTicket(ticket)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if(!finish) {
                                return@subscribe
                            }
                            navigateToMap()
                        },
                        { errorHandler.handleException(it, getBaseActivity()) }
                )
    }

    private fun navigateToMap(){
        startActivity(MainActivity.newIntent(context!!, SelectedPage.MAP))
        getBaseActivity().finish()
    }

    private fun handlerClickerListener() {

        val position = if (location.text == getString(R.string.location_map)) {
            LatLng(ticket.lat, ticket.lng)
        } else {
            null
        }

        startActivityForResult(LocationActivity.newIntent(context!!, ticket.type, position), GET_LOCATION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GET_LOCATION_CODE &&
                resultCode == Activity.RESULT_OK &&
                data != null) {

            val position = data.getParcelableExtra<LatLng>(Intent.EXTRA_TEXT + GET_LOCATION_KEY)
            location.text = getString(R.string.location_map)

            new_ticket_save_button.isActivated = true
            new_ticket_publish_button.isActivated = true

            ticket.lat = position.latitude
            ticket.lng = position.longitude
        }
    }

}