package com.example.zapir.kotopoisk.viewHolder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_temp.*

class ProfileFragmentHolder : BaseFragment() {

    companion object {

        fun newInstance() = BaseFragment()

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //TODO("Заменить на норм разметку")
        return inflater.inflate(R.layout.fragment_temp, container, false)
    }

}