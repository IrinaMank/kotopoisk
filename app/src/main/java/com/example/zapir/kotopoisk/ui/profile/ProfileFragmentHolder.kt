package com.example.zapir.kotopoisk.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.TransactionUtils
import com.example.zapir.kotopoisk.ui.fragment.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers

class ProfileFragmentHolder : BaseFragment() {

    companion object {

        fun newInstance() = ProfileFragmentHolder()

    }

    override fun replaceFragment(fragment: BaseFragment) {
        TransactionUtils.replaceFragment(childFragmentManager, R.id.container, fragment)
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
                    userController.getUser("uxm4WbYgpQFOYcYbuZFF")
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        replaceFragment(ProfileFragment.newInstance(it.get()))
                                    },
                                    {
                                        errorHandler.handleException(it, context!!)
                                    }
                            )
            )

        }
    }
}