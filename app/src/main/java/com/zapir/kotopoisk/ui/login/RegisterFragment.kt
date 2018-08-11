package com.zapir.kotopoisk.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.exceptions.ErrorDialogDisplayer
import com.zapir.kotopoisk.data.exceptions.ExceptionHandler
import com.zapir.kotopoisk.data.model.User
import com.zapir.kotopoisk.ui.base.BaseFragment
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit

class RegisterFragment : BaseFragment() {

    companion object {

        private const val ARG_USER = "Arg_user"

        fun newInstance(user: User): RegisterFragment =
                RegisterFragment().apply {
                    val arguments = Bundle()
                    arguments.putParcelable(ARG_USER, user)
                    this.arguments = arguments
                }

    }

    private val listener by lazy { activity as? LoginActivity }
    private val user: User by lazy {
        arguments?.getParcelable(ARG_USER) as? User ?: throw
        RuntimeException("No user in arguments")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_button.visibility = View.VISIBLE
        back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        toolbar_title.text = getString(R.string.register)

        register_button.setOnClickListener {
            if (validateText()) {
                fillUser()
                (activity as? LoginActivity)?.userController?.getUser(user.id)
                        ?.timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(
                                {
                                    var query: Completable? = null
                                    if (it.isPresent) {
                                        query = (activity as? LoginActivity)?.userController?.updateUser(user)
                                    } else {
                                        query = (activity as? LoginActivity)?.userController?.registerUser(user)
                                    }
                                    query?.timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                                            ?.observeOn(AndroidSchedulers.mainThread())
                                            ?.subscribe(
                                                    {
                                                        listener?.onRegister()
                                                    },
                                                    {
                                                        ExceptionHandler.defaultHandler(listener as ErrorDialogDisplayer).handleException(it, context!!)
                                                    }
                                            )
                                },
                                {
                                    ExceptionHandler.defaultHandler(listener as ErrorDialogDisplayer).handleException(it, context!!)
                                }
                        )
//                (activity as? LoginActivity)?.userController?.registerUser(user)
//                        ?.timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
//                        ?.observeOn(AndroidSchedulers.mainThread())
//                        ?.subscribe(
//                                {
//                                    listener?.onRegister()
//                                },
//                                {
//                                    ExceptionHandler.defaultHandler(listener as ErrorDialogDisplayer).handleException(it, context!!)
//                                }
//                        )
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

    private fun fillUser() {
        user.nickname = nickname_edit_text.text.toString()
        user.name = name_edit_text.text.toString()
        user.email = email_edit_text.text.toString()
        user.phone = phone_edit_text.text.toString()
    }
}
