package com.example.zapir.kotopoisk.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.common.exceptions.ErrorDialogDisplayer
import com.example.zapir.kotopoisk.common.exceptions.ExceptionHandler
import com.example.zapir.kotopoisk.model.User
import com.example.zapir.kotopoisk.ui.fragment.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.concurrent.TimeUnit

class EditProfileFragment : BaseFragment() {
    companion object {

        private const val ARG_USER = "Arg_user"

        fun newInstance(user: User): EditProfileFragment =
                EditProfileFragment().apply {
                    val arguments = Bundle()
                    arguments.putParcelable(ARG_USER, user)
                    this.arguments = arguments
                }

    }

    private val user: User by lazy {
        arguments?.getParcelable(ARG_USER) as? User ?: throw
        RuntimeException("No user in arguments")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillFields()

        register_button.setOnClickListener {
            if (validateText()) {
                fillUser()
                disposables.add((parentFragment as BaseFragment).userController.registerOrUpdateUser
                (user)
                        .timeout(5, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    fragmentManager?.popBackStack()
                                },
                                {
                                    ExceptionHandler.defaultHandler(parentFragment as ErrorDialogDisplayer)
                                            .handleException(it, context!!)
                                }
                        ))
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

    private fun fillFields() {
        nickname_edit_text.setText(user.nickname)
        name_edit_text.setText(user.name)
        email_edit_text.setText(user.email)
        phone_edit_text.setText(user.phone)
    }

    private fun fillUser() {
        user.nickname = nickname_edit_text.text.toString()
        user.name = name_edit_text.text.toString()
        user.email = email_edit_text.text.toString()
        user.phone = phone_edit_text.text.toString()
    }
}