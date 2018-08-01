package com.example.zapir.kotopoisk.domain

import com.example.zapir.kotopoisk.data.model.Ticket
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class RxBus {

    private var newTicketSubject: BehaviorSubject<Ticket>? = null

    fun listenForNewTickets(): Observable<Ticket> {
        if (newTicketSubject == null || newTicketSubject!!.hasComplete() || newTicketSubject!!.hasThrowable()) {
            newTicketSubject = BehaviorSubject.create()
        }

        return newTicketSubject as BehaviorSubject<Ticket>
    }

    fun postNewTicket(ticket: Ticket) {
        if (newTicketSubject == null || newTicketSubject!!.hasComplete() || newTicketSubject!!.hasThrowable()) {
            newTicketSubject = BehaviorSubject.create()
        }

        newTicketSubject!!.onNext(ticket)
    }

}