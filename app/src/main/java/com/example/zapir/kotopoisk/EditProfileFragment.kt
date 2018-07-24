package com.example.zapir.kotopoisk

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.profile_fragment.*

class EditProfileFragment : Fragment() {

    companion object {

        fun newInstance(): EditProfileFragment {
            return EditProfileFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_out_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }

    }

}
