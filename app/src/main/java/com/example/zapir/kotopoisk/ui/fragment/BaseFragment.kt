package com.example.zapir.kotopoisk.ui.fragment

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.ui.BaseActivity
import org.slf4j.LoggerFactory

open class BaseFragment : Fragment() {

    val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    open fun replaceFragment(fragment: BaseFragment) {
        UnsupportedOperationException()
    }

    fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logger.info("onActivityCreated")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logger.info("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logger.info("onViewCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logger.info("onSavedInstanceState")
    }

    override fun onResume() {
        super.onResume()
        logger.info("onResume")
    }

    override fun onStart() {
        super.onStart()
        logger.info("onStart")
    }

    override fun onStop() {
        super.onStop()
        logger.info("onStop")
    }

    override fun onPause() {
        logger.info("onPause")
        super.onPause()
    }

    override fun onDestroy() {
        logger.info("onDestroy")
        super.onDestroy()
    }

}