package com.example.zapir.kotopoisk

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.model.User
import io.reactivex.android.schedulers.AndroidSchedulers


class SplashActivity: AppCompatActivity() {

    val userController = UserFirestoreController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start home activity
        // close splash activity
        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = getSplashScreenDuration()
        Handler().postDelayed(
                {
                    // After the splash screen duration, route to the right activities
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
                // If this is the first launch, make it slow (> 3 seconds) and set flag to false
                sp.edit().putBoolean(prefKeyFirstLaunch, false).apply()
                4000
            }
            false -> {
                // If the user has launched the app, make the splash screen fast (<= 1 seconds)
                2000
            }
        }
    }

    private fun routeToAppropriatePage() {
        userController
                .isAuthorized()
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
                            //ToDO: exception handling
                        }
                )
        finish()
    }
}