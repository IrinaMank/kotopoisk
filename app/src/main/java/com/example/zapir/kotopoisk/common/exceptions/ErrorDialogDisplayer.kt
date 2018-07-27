package com.example.zapir.kotopoisk.common.exceptions

interface ErrorDialogDisplayer {

    fun showOkErrorDialog(msg: Int)
    fun showConnectivityErrorDialog()

}
