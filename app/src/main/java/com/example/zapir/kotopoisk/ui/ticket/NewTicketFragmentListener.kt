package com.example.zapir.kotopoisk.ui.ticket

import com.example.zapir.kotopoisk.model.Ticket

interface NewTicketFragmentListener {
    fun onCreateNewTicket(ticket: Ticket)
}