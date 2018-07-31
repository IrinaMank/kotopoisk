package com.example.zapir.kotopoisk.photo

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.model.Photo
import com.example.zapir.kotopoisk.model.Ticket
import com.example.zapir.kotopoisk.ui.ticket.NewTicketFragment
import com.example.zapir.kotopoisk.ui.ticket.NewTicketFragmentListener
import kotlinx.android.synthetic.main.fragment_ensuring.*

class AddingPhotoFragment: Fragment() {

    companion object {
        const val TAG = "adding photo to an advert"
        private const val INSTANCE_MESSAGE_KEY = "arguments for AddingPhotoFragment"

        fun newInstance(photoUri: Uri): AddingPhotoFragment{
            return AddingPhotoFragment().apply {
                val arguments = Bundle()
                arguments.putParcelable(INSTANCE_MESSAGE_KEY, photoUri)
                setArguments(arguments)
                Log.d("sent instance of", "AddingPhotoFragment")
            }
        }

    }

    private val listener  by lazy {
        context as? NewTicketFragmentListener
                ?: throw Exception("Activity must implement " +
                        "BookFragmentListener")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ensuring, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photoUri = arguments?.getParcelable<Uri>(INSTANCE_MESSAGE_KEY)
                ?: throw Exception("no uri in AddingPhotoFragment arguments")
        super.onViewCreated(view, savedInstanceState)
        photo.setImageURI(photoUri)
        confirmation_button.setOnClickListener { listener.onCreateNewTicket(Ticket(photo = Photo(url = photoUri.toString()))) }
    }

}