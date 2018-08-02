package com.example.zapir.kotopoisk

import android.annotation.SuppressLint
import android.app.Application
import com.example.zapir.kotopoisk.domain.RxBus
import com.example.zapir.kotopoisk.domain.common.PreferencesManager

@SuppressLint("Registered")
class KotopoiskApplication : Application() {


    companion object {

        private val rxBus = RxBus()
        var preferencesManager = PreferencesManager()
        fun preferencesManager() = preferencesManager
        fun rxBus(): RxBus = rxBus

    }

    override fun onCreate() {
        super.onCreate()
        preferencesManager.init(this)
    }

}