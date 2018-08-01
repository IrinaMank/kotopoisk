package com.example.zapir.kotopoisk.domain.bottomBarApi

class BottomBarDelegate {

    var selectionListener: SelectionListener? = null

    interface SelectionListener {
        fun selectPage(position: Int)
    }

}