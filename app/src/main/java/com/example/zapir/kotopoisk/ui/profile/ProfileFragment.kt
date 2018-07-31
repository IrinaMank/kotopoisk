package com.example.zapir.kotopoisk.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.model.User
import com.example.zapir.kotopoisk.ui.fragment.BaseFragment
import com.example.zapir.kotopoisk.ui.login.LoginActivity
import kotlinx.android.synthetic.main.profile_content.*


class ProfileFragment : BaseFragment() {

    companion object {

        private const val ARG_USER = "Arg_user"
        private val userController = UserFirestoreController()

        fun newInstance(user: User): ProfileFragment = ProfileFragment().apply {
            val arguments = Bundle()
            arguments.putParcelable(ARG_USER, user)
            this.arguments = arguments
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile_content, container, false)
    }

    val user: User by lazy {
        arguments?.getParcelable(ProfileFragment.ARG_USER) as? User ?: throw
        RuntimeException("No user in arguments")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_nickname.text = user.nickname
        tv_full_name.text = user.name
        tv_phone.text = user.phone
        tv_email.text = user.email
        tv_photo_pets.text = getString(R.string.pets_discovered, user.petCount)
        tv_found_pets.text = getString(R.string.found_masters, user.foundPetCount)

        if (user.id != preferencesManager.getString(LoginActivity.PREFS_ID)) {
            edit_profile_btn.visibility = View.GONE
            favorite_tickets_btn.visibility = View.GONE
            btn_log_out.visibility = View.GONE
            tv_my_tickets.text = getString(R.string.users_tickets, user.nickname)
        }


        edit_profile_btn.setOnClickListener {
            (parentFragment as BaseFragment).replaceFragment(EditProfileFragment.newInstance(user))
        }

        my_tickets_btn.setOnClickListener {
            (parentFragment as BaseFragment).replaceFragment(MyTicketListFragment.newInstance(user))
        }

        favorite_tickets_btn.setOnClickListener {
            (parentFragment as BaseFragment).replaceFragment(FavotiteTicketsFragment.newInstance(user))
        }

        btn_log_out.setOnClickListener {
            ProfileFragment.userController.logOut()
            val myIntent = Intent(context, LoginActivity::class.java)
            activity?.startActivity(myIntent)
            activity?.finish()

        }

    }


}