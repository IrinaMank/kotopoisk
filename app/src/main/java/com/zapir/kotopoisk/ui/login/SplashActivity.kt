package com.zapir.kotopoisk.ui.login

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AnimationUtils
import com.zapir.kotopoisk.KotopoiskApplication
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.exceptions.ErrorDialogDisplayer
import com.zapir.kotopoisk.data.exceptions.ExceptionHandler
import com.zapir.kotopoisk.domain.firestoreApi.user.UserFirestoreController
import com.zapir.kotopoisk.ui.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit


class SplashActivity : AppCompatActivity(), ErrorDialogDisplayer {

    private val userController = UserFirestoreController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        KotopoiskApplication.preferencesManager.init(this)
        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashScreenDuration()
        Handler().postDelayed(
                {
                    routeToAppropriatePage()
                },
                splashScreenDuration
        )
    }

    private fun getSplashScreenDuration(): Long {
        val prefKeyFirstLaunch = "pref_first_launch"
        return when (KotopoiskApplication.preferencesManager.getBoolean(prefKeyFirstLaunch)) {
            true -> {
                KotopoiskApplication.preferencesManager.putBoolean(prefKeyFirstLaunch, false)
                val animation = paws_image_view.background as AnimationDrawable
                animation.isOneShot = true
                animation.start()
                paws_image_view.postDelayed({
                    val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
                    logo_image_view.visibility = View.VISIBLE
                    logo_image_view.startAnimation(animFadeIn)
                }, 4000)
                6500
            }
            false -> {
                val animFadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
                logo_image_view.visibility = View.VISIBLE
                logo_image_view.startAnimation(animFadeIn)
                2500

            }
        }
    }

    private fun routeToAppropriatePage() {
        userController
                .isAuthorized()
                .timeout(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it == true) {
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            } else {
                                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                            }
                        },
                        {
                            ExceptionHandler.defaultHandler(this).handleException(it, this)
                        }
                )
        finish()
    }

    override fun showOkErrorDialog(msg: Int) {
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(this)
        }
        builder.setTitle(getString(R.string.error_occurred))
                .setMessage(getString(msg) + getString(R.string.app_will_be_closed))
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    finish()//ToDo: is it ok to close activity?
                })
                .show()
    }

    override fun showConnectivityErrorDialog() {
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(this)
        }
        builder.setTitle(getString(R.string.error_occurred))
                .setMessage(getString(R.string.no_connection_error))
                .setPositiveButton(getString(R.string.try_again), { _, _ ->
                    routeToAppropriatePage()
                })
                .show()
    }
}
