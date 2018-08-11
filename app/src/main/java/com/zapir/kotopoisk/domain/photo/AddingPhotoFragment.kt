package com.zapir.kotopoisk.domain.photo

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.model.Photo
import com.zapir.kotopoisk.data.model.Ticket
import com.zapir.kotopoisk.domain.common.SelectedPage
import com.zapir.kotopoisk.ui.base.BaseFragment
import com.zapir.kotopoisk.ui.main.MainActivity
import com.zapir.kotopoisk.ui.ticket.NewTicketFragmentListener
import kotlinx.android.synthetic.main.fragment_ensuring.*

class AddingPhotoFragment : BaseFragment() {

    companion object {
        private const val INSTANCE_MESSAGE_KEY = "arguments for AddingPhotoFragment"

        fun newInstance(photoUri: Uri): AddingPhotoFragment {
            return AddingPhotoFragment().apply {
                val arguments = Bundle()
                arguments.putParcelable(INSTANCE_MESSAGE_KEY, photoUri)
                setArguments(arguments)
                Log.d("sent instance of", "AddingPhotoFragment")
            }
        }

    }

    private val fileSystemManager by lazy { FileSystemManager(getBaseActivity()) }

    private val listener by lazy {
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
        photo.setImageBitmap(fileSystemManager.decodeImageFromUri(photoUri))
        confirmation_button.setOnClickListener { listener.onCreateNewTicket(Ticket(photo = Photo(url = photoUri.toString()))) }
        changing_button.setOnClickListener { returnToMap() }
    }

    private fun returnToMap() {
        startActivity(MainActivity.newIntent(getBaseActivity(), SelectedPage.MAP))
        getBaseActivity().finish()
    }

}