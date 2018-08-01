package com.example.zapir.kotopoisk.ui.ticket

import com.example.zapir.kotopoisk.data.model.Ticket

interface NewTicketFragmentListener {
    fun onCreateNewTicket(ticket: Ticket)
}