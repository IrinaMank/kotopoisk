package com.zapir.kotopoisk.data.exceptions

interface ErrorDialogDisplayer {

    fun showOkErrorDialog(msg: Int)
    fun showConnectivityErrorDialog()

}
