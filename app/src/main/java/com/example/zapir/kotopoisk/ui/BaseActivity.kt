package com.example.zapir.kotopoisk.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.common.exceptions.ErrorDialogDisplayer
import com.example.zapir.kotopoisk.common.exceptions.ExceptionHandler
import com.example.zapir.kotopoisk.firestoreApi.ticket.TicketFirestoreController
import com.example.zapir.kotopoisk.ui.fragment.BaseFragment
import com.example.zapir.kotopoisk.ui.login.LoginFragment
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    val ticketController = TicketFirestoreController()
    val logger = LoggerFactory.getLogger(this.javaClass.simpleName)
    lateinit var disposables: CompositeDisposable

    open fun selectBottomBarTab(position: Int) {
        throw UnsupportedOperationException()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate")
        disposables = CompositeDisposable()
    }

    override fun onStart() {
        super.onStart()
        logger.info("onStart")
    }

    override fun onPause() {
        super.onPause()
        logger.info("onPause")
    }

    override fun onStop() {
        super.onStop()
        logger.info("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.info("onDestroy")
        disposables.dispose()
    }



}