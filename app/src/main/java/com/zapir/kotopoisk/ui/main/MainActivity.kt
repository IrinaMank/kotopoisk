package com.zapir.kotopoisk.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.view.MenuItem
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.domain.bottomBarApi.BottomBarPagerAdapter
import com.zapir.kotopoisk.domain.common.SelectedPage
import com.zapir.kotopoisk.domain.photo.AddPhotoListener
import com.zapir.kotopoisk.domain.photo.PhotoHandler
import com.zapir.kotopoisk.domain.photo.TicketActivity
import com.zapir.kotopoisk.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), AddPhotoListener {

    lateinit var currentPhotoPath: String
    lateinit var photoURI: Uri

    private var toolbar: ActionBar? = null
    private var prevMenuItem: MenuItem? = null

    private var startPage: SelectedPage = SelectedPage.MAP

    companion object {

        private const val START_PAGE = "flag key for intent"
        fun newIntent(context: Context, selectedPage: SelectedPage): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(START_PAGE, selectedPage)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            return intent
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent!!)
        selectBottomBarTab(startPage.value)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null && intent.hasExtra(START_PAGE)) {
            startPage = intent.getSerializableExtra(START_PAGE) as SelectedPage
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        handleIntent(intent)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initBottomNavigationViewHelper()
        selectBottomBarTab(startPage.value)
    }

    override fun selectBottomBarTab(position: Int) {
        logger.info("Navigation: ${navigation_view.menu.getItem(position).title}")
        pager.currentItem = position
        toolbar?.title = navigation_view.menu.getItem(position).title
    }

    private fun initBottomNavigationViewHelper() {
        // Set toolbar
        toolbar = supportActionBar

        // Set BottomBarPagerAdapter
        pager.adapter = BottomBarPagerAdapter(supportFragmentManager)

        // Set listener item selected
        navigation_view.setOnNavigationItemSelectedListener {
            handlerNavigationListener(it)
        }

        // Set listener change item
        pager.addOnPageChangeListener(handlerPageChangeListener)
        pager.offscreenPageLimit = SelectedPage.values().size
    }

    override fun onBackPressed() {
        val currentHostFragment: Fragment? = supportFragmentManager.findFragmentByTag(
                "android:switcher:" + R.id.pager + ":" + pager.currentItem)

        if (currentHostFragment != null) {
            val childManager = currentHostFragment.childFragmentManager

            if (childManager.backStackEntryCount > 1) {
                childManager.popBackStack()
                return
            }
        }

        super.onBackPressed()
    }

    private fun handlerNavigationListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_search -> selectBottomBarTab(SelectedPage.SEARCH.value)
            R.id.navigation_map -> selectBottomBarTab(SelectedPage.MAP.value)
            R.id.navigation_profile -> selectBottomBarTab(SelectedPage.PROFILE.value)
            else -> return false
        }

        return true
    }

    private val handlerPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageSelected(position: Int) {
            prevMenuItem?.isChecked = false
            navigation_view.menu.getItem(position).isChecked = true
            selectBottomBarTab(position)
        }

    }

    override fun addPhotoToAdvert(photoUri: Uri) {
        startActivity(TicketActivity.newIntent(this, photoUri))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PhotoHandler.CAMERA_CAPTURE && resultCode == RESULT_OK) {
            addPhotoToAdvert(photoURI)
        } else if (requestCode == PhotoHandler.OPEN_GALLERY && resultCode == RESULT_OK) {
            val imageUri = data?.data ?: throw Exception("empty data in OPEN_GALLERY")
            addPhotoToAdvert(imageUri)
        }
    }

}