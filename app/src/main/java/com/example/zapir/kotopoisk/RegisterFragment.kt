package com.example.zapir.kotopoisk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ch.qos.logback.core.net.SocketConnector
import com.example.zapir.kotopoisk.common.ExceptionHandler
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*

class RegisterFragment: Fragment() {

    companion object {

        private const val ARG_USER = "Arg_user"

        fun newInstance(user: User): RegisterFragment =
                RegisterFragment().apply {
                    val arguments = Bundle()
                    arguments.putParcelable(ARG_USER, user)
                    this.arguments = arguments
                }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user: User by lazy {
            arguments?.getParcelable(ARG_USER) as? User ?: throw
            RuntimeException("No user in arguments")
        }

        nickname_edit_text.setText(user.nickname)
        name_edit_text.setText(user.name)
        email_edit_text.setText(user.email)
        phone_edit_text.setText(user.phone)

        register_button.setOnClickListener {
            if (validateText()) {
                //            val id = activity?.getSharedPreferences("UserPrefs", Context
//                    .MODE_PRIVATE)?.getString(LoginActivity.PREFS_ID, "") ?:
//            UUID.randomUUID().toString()
//            val user: User = User(
//                    id = id,
//                    nickname = nickname_edit_text.text.toString(),
//                    name = name_edit_text.text.toString(),
//                    email = email_edit_text.text.toString(),
//                    phone = phone_edit_text.text.toString())
                (activity as? LoginActivity)?.userController?.registerOrUpdateUser(user)
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(
                                {
                                    startActivity(Intent(context, MainActivity::class.java))
                                    activity?.finish()
                                },
                                {

                                }
                        )
            }
        }

    }

    private fun validateText() =
            when {
                    nickname_edit_text.text.isEmpty() -> {
                        nickname_edit_text.requestFocus()
                        nickname_edit_text.error = getString(R.string.please_fill_field)
                    false
                }
                name_edit_text.text.isEmpty() -> {
                    name_edit_text.requestFocus()
                    name_edit_text.error = getString(R.string.please_fill_field)
                    false
                }
                phone_edit_text.text.isEmpty() -> {
                    phone_edit_text.requestFocus()
                    phone_edit_text.error = getString(R.string.please_fill_field)
                    false
                }
                else -> true
            }
}
