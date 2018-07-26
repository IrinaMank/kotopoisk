package com.example.zapir.kotopoisk

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginFragment = LoginFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, loginFragment)
                .addToBackStack(LoginFragment.TAG)
                .commit()

    }
}