package com.example.zapir.kotopoisk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_auth.*


class LoginFragment: Fragment() {

    companion object {

        fun newInstance(): LoginFragment = LoginFragment()

        const val TAG = "Login Fragment"
        private const val AUTH_ID = "979610091334-rm9dte6qqcsb9jc167fqcqh9f7ucn9cc.apps" +
                ".googleusercontent.com"
        private const val PREFS_TOKEN = "Saved pref token"
        private const val PREFS_NAME = "Saved pref name"
        private const val PREFS_EMAIL = "Saved pref email"
        private const val RC_SIGN_IN = 9001


    }

    private val sharedPrefs: SharedPreferences? by lazy { activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) }
    private val userController = UserFirestoreController()
    private var googleSignInClient: GoogleSignInClient? = null
    private val listener by lazy { activity as? LoginActivity }
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(AUTH_ID)
                .build()
        googleSignInClient = GoogleSignIn.getClient(context!!, gso)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_in_google_button.setOnClickListener {
            sign_in_google_button.isEnabled = false
            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                listener?.userController?.logInWithGoogle(account)
                        ?.observeOn(AndroidSchedulers.mainThread())
                        ?.subscribe(
                                {
                                    val sharedPrefs: SharedPreferences? =  activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                    sharedPrefs?.edit()?.putString(LoginActivity.PREFS_ID, it
                                            .id)?.apply()
                                    listener?.onLogin(it)
                                },
                                {
                                    //ToDo: exception handling
                                }
                        )
            } catch (e: ApiException) {
                //
            }

        }
    }

}
