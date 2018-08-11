package com.zapir.kotopoisk.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.domain.bottomBarApi.TransactionUtils
import com.zapir.kotopoisk.ui.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers

class ProfileFragmentHolder : BaseFragment() {

    companion object {

        fun newInstance() = ProfileFragmentHolder()

    }

    override fun replaceFragment(fragment: BaseFragment) {
        TransactionUtils.replaceFragment(childFragmentManager, R.id.container, fragment)
    }

    override fun addFragment(fragment: BaseFragment) {
        TransactionUtils.addFragment(childFragmentManager, R.id.container, fragment)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_holder, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (TransactionUtils.isEmpty(childFragmentManager)) {
            disposables.add(
                    userController.getCurrentUser()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        replaceFragment(ProfileFragment.newInstance(it))
                                    },
                                    {
                                        errorHandler.handleException(it, context!!)
                                    }
                            )
            )

        }
    }
}