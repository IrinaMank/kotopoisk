package com.zapir.kotopoisk.ui.tickets_recycler

import com.zapir.kotopoisk.data.model.Ticket

interface OnItemClickListener {
    fun onItemClick(ticket: Ticket)
    fun onFavorClick(ticket: Ticket)
}