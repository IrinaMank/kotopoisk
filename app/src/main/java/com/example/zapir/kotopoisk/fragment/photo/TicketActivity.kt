package com.example.zapir.kotopoisk.fragment.photo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.activity.BaseActivity

class TicketActivity: BaseActivity() {

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

        val photoUri = intent.getParcelableExtra<Uri>(NEW_INSTANCE_OF)
        supportFragmentManager.beginTransaction()
                .add(R.id.container, AddingPhoto.newInstance(photoUri))
                .commit()
    }
}