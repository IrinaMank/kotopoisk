package com.example.zapir.kotopoisk.ui.login

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.example.zapir.kotopoisk.common.exceptions.ErrorDialogDisplayer
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash.*
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import com.example.zapir.kotopoisk.MainActivity
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.common.exceptions.ExceptionHandler
import java.util.concurrent.TimeUnit


class SplashActivity: AppCompatActivity(), ErrorDialogDisplayer {

    val userController = UserFirestoreController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val animFadeIn = AnimationUtils.loadAnimation(applicationContext,
                R.anim.fade_in);
        splash_text_title.visibility = View.VISIBLE;
        splash_text_title.startAnimation(animFadeIn);
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
        val sp = getPreferences(Context.MODE_PRIVATE)
        val prefKeyFirstLaunch = "pref_first_launch"

        return when(sp.getBoolean(prefKeyFirstLaunch, true)) {
            true -> {
                sp.edit().putBoolean(prefKeyFirstLaunch, false).apply()
                5000
            }
            false -> {
                3000
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
                            if(it == true) {
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
                .setMessage(getString(msg)+getString(R.string.app_will_be_closed))
                .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, which ->
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
                .setPositiveButton(getString(R.string.try_again), DialogInterface.OnClickListener { dialog,
                                                                                                    which ->
                    routeToAppropriatePage()
                })
                .show()
    }
}