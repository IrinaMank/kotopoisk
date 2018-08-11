package com.zapir.kotopoisk.ui.ticket

import com.zapir.kotopoisk.data.model.Ticket

interface NewTicketFragmentListener {
    fun onCreateNewTicket(ticket: Ticket)
}