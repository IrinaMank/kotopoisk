package com.example.zapir.kotopoisk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.widget.Toast
import com.example.zapir.kotopoisk.R.id.text
import com.example.zapir.kotopoisk.common.ErrorDialogDisplayer
import com.example.zapir.kotopoisk.common.ExceptionHandler
import com.example.zapir.kotopoisk.firestoreApi.ticket.TicketFirestoreController
import com.example.zapir.kotopoisk.firestoreApi.user.UserFirestoreController
import com.example.zapir.kotopoisk.model.Ticket
import com.example.zapir.kotopoisk.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), ErrorDialogDisplayer  {

    //it's just for test
    override fun showOkErrorDialog(msg: Int) {
        Toast.makeText(this, "ETO OK DIALOG)))0)))", Toast.LENGTH_LONG).show()
    }

    override fun showConnectivityErrorDialog() {
        Toast.makeText(this, "ETO OY OY CONNECTION FUUU DIALOG)))0)))", Toast.LENGTH_LONG).show()
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_search -> {
                logger.info("Navigation: search")
                toolbar.title = getString(R.string.toolbar_string_search)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_map -> {
                logger.info("Navigation: map")
                toolbar.title = getString(R.string.toolbar_string_map)
                val mapFragment = MapFragment.newInstance()
                openFragment(mapFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_profile -> {
                logger.info("Navigation: profile")
                toolbar.title = getString(R.string.toolbar_string_profile)
                val handler = ExceptionHandler.defaultHandler(this)
                val co = TicketFirestoreController()
                val user = User(id="dfrfrf", nickname = "Ira")
                co.getAllTickets().observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {

                                    Toast.makeText(this, "opaaaaaaa OK DIALOG)))0)))", Toast
                                            .LENGTH_LONG).show()

                                },
                                {
                                    handler.handleException(it)
                                }
                        )
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = supportActionBar!!
        navigation_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

}
