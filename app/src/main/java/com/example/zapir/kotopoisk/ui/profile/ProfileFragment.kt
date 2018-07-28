package com.example.zapir.kotopoisk.ui.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.model.User
import com.example.zapir.kotopoisk.ui.login.LoginFragment
import kotlinx.android.synthetic.main.profile_content.*
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.zapir.kotopoisk.firestoreApi.ticket.TicketFirestoreController
import com.example.zapir.kotopoisk.ui.login.LoginActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.File
import com.google.common.io.Flushables.flush
import android.graphics.Bitmap
import android.R.attr.bitmap
import android.net.Uri
import java.io.FileOutputStream
import java.io.OutputStream


class ProfileFragment: Fragment() {

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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user: User by lazy {
            arguments?.getParcelable(ARG_USER) as? User ?: throw
            RuntimeException("No user in arguments")
        }

        tv_nickname.text = user.nickname
        tv_full_name.text = user.name
        tv_phone.text = user.phone
        tv_email.text = user.email


        edit_profile_btn.setOnClickListener {
            //ToDo: call edit fragment
        }

        btn_log_out.setOnClickListener {
            userController.logOut()
            val myIntent = Intent(context, LoginActivity::class.java)
            activity?.startActivity(myIntent)
            activity?.finish()

        }

        TicketFirestoreController().getPhoto("111").observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            Glide.with(this)
                                    .load(it.url)
                                    .into(this.profile_icon)
                        },
                        {}
                )


        val drawable: BitmapDrawable = ContextCompat.getDrawable(context!!, R.drawable.logo) as
                BitmapDrawable

        // Get the bitmap from drawable object
        val bitmap = drawable.bitmap


        val path = Environment.getExternalStorageDirectory().toString()
        val file2 = File(Uri.parse("android.resource://" + R::class.java.`package`.name + "/" +
                R.drawable.ic_email_black_24dp).toString())

        TicketFirestoreController().uploadPhoto(file2, "1112").observeOn(AndroidSchedulers
                .mainThread()).subscribe(
                {
                    tv_full_name.text ="SDFDHFUDHFUDFHEUFHRUFHRU"
                },
                {

                }
        )
    }
}
