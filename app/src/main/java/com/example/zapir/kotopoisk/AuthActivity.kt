package com.example.zapir.kotopoisk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*


class AuthActivity : AppCompatActivity() {

    companion object {
        private const val AUTH_ID = "979610091334-rm9dte6qqcsb9jc167fqcqh9f7ucn9cc.apps" +
                ".googleusercontent.com"
        private const val PREFS_TOKEN = "Saved pref token"
        private const val PREFS_NAME = "Saved pref name"
        private const val PREFS_EMAIL = "Saved pref email"
        private val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


    private val sharedPrefs: SharedPreferences by lazy {
        this.getSharedPreferences("UserPrefs",
                Context.MODE_PRIVATE)
    }

    // [START declare_auth]
    private var mAuth: FirebaseAuth? = null
    // [END declare_auth]

    private var googleSignInClient: GoogleSignInClient? = null
    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(AUTH_ID)
                .build()
        mAuth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_google_button.setOnClickListener {
            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    public override fun onStart() {
        super.onStart()
        //val token = sharedPrefs.getString(PREFS_TOKEN, "")
        val currentUser = mAuth?.currentUser
        if (currentUser != null) {
            updateUI()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                //
            }

        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val editor = sharedPrefs.edit()
        if (mAuth != null) {
            mAuth?.signInWithCredential(credential)?.addOnCompleteListener(this, { task ->
                if (task.isSuccessful) {
                    val user = mAuth?.currentUser
                    editor?.putString(PREFS_TOKEN, account.idToken)
                    editor?.putString(PREFS_NAME, user
                            ?.getDisplayName())
                    editor?.putString(PREFS_EMAIL, user?.email)
                    editor?.apply()
                    updateUI()
                } else {
                    //Snackbar.make(findViewById(R.id.activity_auth), "Authentication " +
                    // "Failed.", Snackbar.LENGTH_SHORT).show(); //ToDo:
                    // connection failed
                }

            })
        }

    }

    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
