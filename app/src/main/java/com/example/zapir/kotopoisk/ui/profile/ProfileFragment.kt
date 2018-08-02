package com.example.zapir.kotopoisk.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.KotopoiskApplication
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.User
import com.example.zapir.kotopoisk.domain.bottomBarApi.TransactionUtils
import com.example.zapir.kotopoisk.domain.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.ui.base.BaseActivity
import com.example.zapir.kotopoisk.ui.base.BaseFragment
import com.example.zapir.kotopoisk.ui.login.LoginActivity
import kotlinx.android.synthetic.main.profile_content.*
import kotlinx.android.synthetic.main.toolbar.*


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

    override fun onResume() {
        super.onResume()
        tv_photo_pets.text = getString(R.string.pets_discovered, user.petCount)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_nickname.text = user.nickname
        tv_full_name.text = user.name
        tv_phone.text = user.phone
        tv_email.text = user.email
        number_btn.setOnClickListener{
            val number = Uri.parse("tel:"+tv_phone.text.toString())
            val callIntent = Intent(Intent.ACTION_DIAL, number)
            startActivity(Intent.createChooser(callIntent, "Позвонить"))
        }

        email_btn.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_SEND, Uri.parse("mailto:" + tv_email.text.toString()))
            //callIntent.type = "application/octet-stream"
            startActivity(Intent.createChooser(callIntent, "Написать письмо"))
        }

        tv_photo_pets.text = getString(R.string.pets_discovered, user.petCount)
        tv_found_pets.text = getString(R.string.found_masters, user.foundPetCount)

        if (user.id != KotopoiskApplication.preferencesManager.getString(LoginActivity.PREFS_ID)) {
            edit_profile_btn.visibility = View.GONE
            favorite_tickets_btn.visibility = View.GONE
            btn_log_out.visibility = View.GONE
            tv_my_tickets.text = getString(R.string.his_tickets)
        }


        edit_profile_btn.setOnClickListener {
            (parentFragment as? BaseFragment)?.replaceFragment(EditProfileFragment.newInstance
            (user))
        }

        my_tickets_btn.setOnClickListener {
            if (user.id != KotopoiskApplication.preferencesManager.getString(LoginActivity.PREFS_ID)) {
                val manager = (context as BaseActivity).supportFragmentManager
                TransactionUtils.replaceFragment(manager, R.id.container, MyTicketListFragment.newInstance(user))
            } else {
                (parentFragment as? BaseFragment)?.replaceFragment(MyTicketListFragment.newInstance(user))
            }
        }

        favorite_tickets_btn.setOnClickListener {
            (parentFragment as? BaseFragment)?.replaceFragment(FavoriteTicketsFragment.newInstance
            (user))
        }

        btn_log_out.setOnClickListener {
            ProfileFragment.userController.logOut()
            val myIntent = Intent(context, LoginActivity::class.java)
            activity?.startActivity(myIntent)
            activity?.finish()

        }

        toolbar_title.text = getString(R.string.toolbar_string_profile)

        if(activity?.supportFragmentManager?.backStackEntryCount!! > 0)
        {
            back_button.visibility = View.VISIBLE
            back_button.setOnClickListener {
                activity?.onBackPressed()
            }
        }

    }


}