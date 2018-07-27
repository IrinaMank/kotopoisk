package com.example.zapir.kotopoisk

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity: AppCompatActivity() {

    val userController = UserFirestoreController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Start home activity
        // close splash activity
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
                5000
            }
            false -> {
                // If the user has launched the app, make the splash screen fast (<= 1 seconds)
                3000
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