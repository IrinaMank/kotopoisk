package com.zapir.kotopoisk.data.exceptions

import android.content.Context
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.domain.common.NetworkUtils
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

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

    fun handleException(ex: Throwable, context: Context) {
        when (ex) {
            is TimeoutException -> {
                if (NetworkUtils.isNetworkAvailable(context)) {
                    doOnApiException(ApiBaseException())
                } else {
                    doOnNoConnectivity(NoConnectivityException())
                }
            }
            is ApiBaseException -> doOnApiException(ex)
            is NoConnectivityException -> doOnNoConnectivity(ex)
            else -> doOnNotApiException(ex)
        }
    }

}
