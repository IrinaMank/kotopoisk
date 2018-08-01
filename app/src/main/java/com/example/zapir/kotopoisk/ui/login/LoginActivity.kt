package com.example.zapir.kotopoisk.ui.login

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.exceptions.ErrorDialogDisplayer
import com.example.zapir.kotopoisk.data.exceptions.ExceptionHandler
import com.example.zapir.kotopoisk.data.model.User
import com.example.zapir.kotopoisk.domain.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.ui.base.BaseActivity
import com.example.zapir.kotopoisk.ui.main.MainActivity
import com.example.zapir.kotopoisk.ui.map.LoadListener
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity(), ErrorDialogDisplayer, LoadListener {

    override fun setLoadStart() {
        auth_progress_bar.visibility = View.VISIBLE
        val animation = auth_progress_bar.background as AnimationDrawable
        animation.start()
    }

    override fun setLoadGone() {
        auth_progress_bar.visibility = View.GONE
    }

    companion object {
        const val PREFS_ID = "PREFS_ID"
    }

    val userController = UserFirestoreController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        returnToLoginFragment()
    }

    fun onLogin(user: User) {
        setLoadStart()
        supportFragmentManager.popBackStack()
        userController.getUser(userId = user.id).observeOn(AndroidSchedulers.mainThread())
                .timeout(5, TimeUnit.SECONDS)
                .subscribe(
                        {
                            if (it.isPresent) {
                                setLoadGone()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                supportFragmentManager.beginTransaction()
                                        .replace(R.id.login_container, RegisterFragment.newInstance(user))
                                        .addToBackStack(LoginFragment.TAG)
                                        .commit()
                            }
                        },
                        {
                            ExceptionHandler.defaultHandler(this).handleException(it, this)
                        }
                )


    }

    fun onRegister() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    override fun showOkErrorDialog(msg: Int) {
        Toast.makeText(this, "Sorry, some problems with authentification. Please, try again",
                Toast.LENGTH_LONG).show()
        returnToLoginFragment()
    }

    override fun showConnectivityErrorDialog() {
        Toast.makeText(this, R.string.no_connection_error, Toast.LENGTH_LONG).show()
        returnToLoginFragment()
    }

    private fun returnToLoginFragment() {
        val loginFragment = LoginFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.login_container, loginFragment)
                .commit()
    }
}
