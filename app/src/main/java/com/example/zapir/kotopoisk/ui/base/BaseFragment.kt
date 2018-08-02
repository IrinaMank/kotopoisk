package com.example.zapir.kotopoisk.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.exceptions.ErrorDialogDisplayer
import com.example.zapir.kotopoisk.data.exceptions.ExceptionHandler
import com.example.zapir.kotopoisk.domain.bottomBarApi.TransactionUtils
import com.example.zapir.kotopoisk.domain.firestoreApi.ticket.TicketFirestoreController
import com.example.zapir.kotopoisk.domain.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.ui.login.LoginFragment
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory

open class BaseFragment : Fragment(), ErrorDialogDisplayer {

    val logger = LoggerFactory.getLogger(this.javaClass.simpleName)
    lateinit var disposables: CompositeDisposable
    val ticketController = TicketFirestoreController()
    val userController = UserFirestoreController()
    val errorHandler = ExceptionHandler.defaultHandler(this) //ToDo: warning

    open fun replaceFragment(fragment: BaseFragment) {
        UnsupportedOperationException()
    }

    open fun addFragment(fragment: BaseFragment) {
        UnsupportedOperationException()
    }

    fun getBaseActivity(): BaseActivity {
        return activity as BaseActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate")
        disposables = CompositeDisposable()
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
        disposables.dispose()
    }

    override fun showOkErrorDialog(msg: Int) {
        Toast.makeText(activity, "Sorry, some problems with authentification. Please, try again",
                Toast.LENGTH_LONG).show()

    }

    override fun showConnectivityErrorDialog() {
        Toast.makeText(activity, R.string.no_connection_error, Toast.LENGTH_LONG).show()
    }

    private fun returnToLoginFragment() {
        val loginFragment = LoginFragment.newInstance()
        TransactionUtils.replaceFragment(childFragmentManager, R.id.container, loginFragment)
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}