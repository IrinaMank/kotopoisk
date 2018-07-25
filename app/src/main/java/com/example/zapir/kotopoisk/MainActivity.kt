package com.example.zapir.kotopoisk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import com.example.zapir.kotopoisk.firestoreApi.FirestoreController
import com.example.zapir.kotopoisk.model.Ticket
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

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
        val co = FirestoreController()
        val ticket = Ticket(overview = "test")
        co.publishTicket(ticket, {})
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

}
