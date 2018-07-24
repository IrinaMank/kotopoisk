package com.example.zapir.kotopoisk

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.slf4j.LoggerFactory

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate")
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
    }

}