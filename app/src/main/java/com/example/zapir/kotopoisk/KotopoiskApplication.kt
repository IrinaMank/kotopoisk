package com.example.zapir.kotopoisk

import android.annotation.SuppressLint
import android.app.Application
import com.example.zapir.kotopoisk.domain.RxBus

@SuppressLint("Registered")
class KotopoiskApplication : Application() {

    companion object {

        private val rxBus = RxBus()
        fun getRxBus(): RxBus = rxBus

    }

    override fun onCreate() {
        super.onCreate()
    }

}