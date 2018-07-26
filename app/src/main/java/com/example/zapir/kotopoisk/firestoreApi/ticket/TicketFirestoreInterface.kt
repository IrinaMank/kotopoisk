package com.example.zapir.kotopoisk.firestoreApi.ticket

import com.example.zapir.kotopoisk.model.Ticket
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

interface TicketFirestoreInterface {

    fun getAllTickets(): Single<List<Ticket>>
    fun getTicket(tickedId: String): Single<Ticket?>
    fun getUserTickets(userId: String): Single<List<Ticket>>
    fun getSavedTickets(userId: String): Single<List<Ticket>>
    fun getFavouriteTickets(userId: String): Single<List<Ticket>>
//    fun searchTicket(ticket: Ticket, radius: Double, completion: (List<Ticket>) -> Unit)
    fun uploadTicket(ticket: Ticket): Single<Unit>
    fun publishTicket(ticket: Ticket): Single<Unit>
    fun updateTicket(newTicket: Ticket): Single<Unit>
    fun deleteTicket(ticket: Ticket): Single<Unit>
    fun makeTicketFavourite(ticket: Ticket): Single<Unit>
    fun makeTicketUnFavourite(ticket: Ticket): Single<Unit>
//
    fun uploadPhoto(file: File): Single<String>//return URI of upload file

}