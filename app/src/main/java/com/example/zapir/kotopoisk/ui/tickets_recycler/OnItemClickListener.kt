package com.example.zapir.kotopoisk.ui.tickets_recycler

import com.example.zapir.kotopoisk.data.model.Ticket

interface OnItemClickListener {
    fun onItemClick(ticket: Ticket)
}