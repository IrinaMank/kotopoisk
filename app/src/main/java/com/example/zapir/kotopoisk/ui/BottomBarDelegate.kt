package com.example.zapir.kotopoisk.ui

class BottomBarDelegate {

    var selectionListener: SelectionListener? = null

    interface SelectionListener {
        fun selectPage(position: Int)
    }

}