package com.example.zapir.kotopoisk.common

import android.content.Context
import android.net.ConnectivityManager


class NetworkUtils private constructor() {

    init {
        throw UnsupportedOperationException()
    }

    companion object {

        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }
}