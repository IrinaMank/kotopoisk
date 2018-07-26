package com.example.zapir.kotopoisk

import android.content.Context
import android.content.SharedPreferences
import android.support.v4.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient


class LoginFragment: Fragment() {

    companion object {

        const val TAG = "Login Fragment"
        fun newInstance(): LoginFragment = LoginFragment()
        private const val AUTH_ID = "979610091334-rm9dte6qqcsb9jc167fqcqh9f7ucn9cc.apps" +
                ".googleusercontent.com"
        private const val PREFS_TOKEN = "Saved pref token"
        private const val PREFS_NAME = "Saved pref name"
        private const val PREFS_EMAIL = "Saved pref email"
        private const val RC_SIGN_IN = 9001

    }

    private val sharedPrefs: SharedPreferences? by lazy { activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) }

    private var googleSignInClient: GoogleSignInClient? = null
    private var googleApiClient: GoogleApiClient? = null



}
