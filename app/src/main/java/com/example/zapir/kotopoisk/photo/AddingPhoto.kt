package com.example.zapir.kotopoisk.photo

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import kotlinx.android.synthetic.main.fragment_ensuring.*

class AddingPhoto: Fragment() {

    companion object {
        const val TAG = "adding photo to an advert"
        private const val INSTANCE_MESSAGE_KEY = "arguments for AddingPhoto"

        fun newInstance(photoUri: Uri): AddingPhoto{
            return AddingPhoto().apply {
                val arguments = Bundle()
                arguments.putParcelable(INSTANCE_MESSAGE_KEY, photoUri)
                setArguments(arguments)
                Log.d("sent instance of", "AddingPhoto")
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ensuring, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val photoUri = arguments?.getParcelable<Uri>(INSTANCE_MESSAGE_KEY)
                ?: throw Exception("no uri in AddingPhoto arguments")
        super.onViewCreated(view, savedInstanceState)
        photo.setImageURI(photoUri)
    }

}