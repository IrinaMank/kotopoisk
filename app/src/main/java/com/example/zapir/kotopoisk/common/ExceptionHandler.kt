package com.example.zapir.kotopoisk.common

import com.example.zapir.kotopoisk.R
import com.google.firebase.firestore.FirebaseFirestoreException
import java.net.SocketTimeoutException

class ExceptionHandler(val doOnApiException: (ApiBaseException) -> Unit,
                       val doOnNotApiException: (Throwable) -> Unit,
                       val doOnNoConnectivity: (NoConnectivityException) -> Unit) {


    companion object {
        fun defaultHandler(dialogDisplayer: ErrorDialogDisplayer): ExceptionHandler {
            return ExceptionHandler(
                    doOnApiException = {
                        dialogDisplayer.showOkErrorDialog(it.getDefaultRationale())
                    },
                    doOnNoConnectivity = {
                        dialogDisplayer.showConnectivityErrorDialog()
                    },
                    doOnNotApiException = {
                        if (it is SocketTimeoutException) {
                            dialogDisplayer.showOkErrorDialog(R.string.timout_error)
                        } else {
                            dialogDisplayer.showConnectivityErrorDialog()
                        }
                        }
            )
        }
    }

    fun handleException(ex: Throwable) {
        when(ex) {
            is ApiBaseException -> doOnApiException(ex)
            is NoConnectivityException -> doOnNoConnectivity(ex)
            else -> doOnNotApiException(ex)
        }
    }

}