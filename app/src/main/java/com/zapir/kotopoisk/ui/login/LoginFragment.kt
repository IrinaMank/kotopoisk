package com.zapir.kotopoisk.ui.login

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.zapir.kotopoisk.KotopoiskApplication
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.exceptions.ErrorDialogDisplayer
import com.zapir.kotopoisk.data.exceptions.ExceptionHandler
import com.zapir.kotopoisk.domain.common.MyBounceInterpolator
import com.zapir.kotopoisk.ui.base.BaseFragment
import com.zapir.kotopoisk.ui.map.LoadListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_auth.*
import java.util.concurrent.TimeUnit


class LoginFragment : BaseFragment(), LoadListener {
    override fun setLoadStart() {
        auth_progress_bar.visibility = View.VISIBLE
        val animation = auth_progress_bar.background as AnimationDrawable
        animation.start()
    }

    override fun setLoadGone() {
        auth_progress_bar.visibility = View.GONE
    }

    companion object {

        fun newInstance(): LoginFragment = LoginFragment()

        const val TAG = "Login Fragment"
        private const val AUTH_ID = "979610091334-rm9dte6qqcsb9jc167fqcqh9f7ucn9cc.apps" +
                ".googleusercontent.com"
        private const val RC_SIGN_IN = 9001


    }

    private var googleSignInClient: GoogleSignInClient? = null
    private val listener by lazy { activity as? LoginActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(AUTH_ID)
                .build()
        googleSignInClient = GoogleSignIn.getClient(context!!, gso)
        KotopoiskApplication.preferencesManager.init(context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sign_in_google_button.setOnClickListener {
            val myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce)
            val interpolator = MyBounceInterpolator(0.2, 20.0)
            myAnim.interpolator = interpolator
            sign_in_google_button.startAnimation(myAnim)
            sign_in_google_button.isEnabled = false
            val signInIntent = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        setLoadStart()

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            listener?.disposables?.add(listener?.userController?.logInWithGoogle(account)
                    ?.timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                            {
                                setLoadGone()
                                KotopoiskApplication.preferencesManager.putString(LoginActivity
                                        .PREFS_ID, it.id)
                                listener?.onLogin(it)
                            },
                            {
                                ExceptionHandler.defaultHandler(listener as ErrorDialogDisplayer).handleException(it, context!!)
                            }
                    ) ?: throw RuntimeException("Error while creating observable")
            )
        }

    }

}
