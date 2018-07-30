package com.example.zapir.kotopoisk.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.common.exceptions.ErrorDialogDisplayer
import com.example.zapir.kotopoisk.common.exceptions.ExceptionHandler
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.model.User
import com.example.zapir.kotopoisk.ui.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity(), ErrorDialogDisplayer {

    companion object {
        const val PREFS_ID = "User prefs"
    }

    val userController = UserFirestoreController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        returnToLoginFragment()
    }

    fun onLogin(user: User) {
        supportFragmentManager.popBackStack()
        userController.getUser(userId = user.id).observeOn(AndroidSchedulers.mainThread())
                .timeout(5, TimeUnit.SECONDS)
                .subscribe(
                        {
                            if (it.isPresent) {
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
