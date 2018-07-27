package com.example.zapir.kotopoisk

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.zapir.kotopoisk.common.NotFoundObject
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity: AppCompatActivity() {

    companion object {

        const val PREFS_ID= "Saved pref id"
    }

    val userController = UserFirestoreController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginFragment = LoginFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.login_container, loginFragment)
                .commit()
    }

    fun onLogin(user: User) {
        supportFragmentManager.popBackStack()
        userController.getUser(userId = user.id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        },
                        {
                            if (it is NotFoundObject) {
                                supportFragmentManager.beginTransaction()
                                        .replace(R.id.login_container, RegisterFragment
                                                .newInstance(user))
                                        .addToBackStack(LoginFragment.TAG)
                                        .commit()
                            }
                        }
                )



    }
}