package com.example.zapir.kotopoisk.domain.photo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.ui.base.BaseActivity
import com.example.zapir.kotopoisk.ui.ticket.NewTicketFragment
import com.example.zapir.kotopoisk.ui.ticket.NewTicketFragmentListener

class TicketActivity: BaseActivity(), NewTicketFragmentListener {

    companion object {
        private val NEW_INSTANCE_OF = "Ticket Activity"

        fun newIntent(context: Context, photoUri: Uri): Intent {
            val intent = Intent(context, TicketActivity::class.java)
            intent.putExtra(NEW_INSTANCE_OF, photoUri)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_holder)

        supportActionBar?.title = getString(R.string.new_ticket)

        val photoUri = intent.getParcelableExtra<Uri>(NEW_INSTANCE_OF)
        supportFragmentManager.beginTransaction()
                .add(R.id.container, AddingPhotoFragment.newInstance(photoUri))
                .commit()
    }

    override fun onCreateNewTicket(ticket: Ticket){
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewTicketFragment.newInstance(ticket))
                .commit()
    }

}