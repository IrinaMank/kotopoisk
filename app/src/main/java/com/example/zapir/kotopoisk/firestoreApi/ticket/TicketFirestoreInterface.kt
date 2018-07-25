package com.example.zapir.kotopoisk.firestoreApi.ticket

import com.example.zapir.kotopoisk.model.Ticket
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

interface TicketFirestoreInterface {

    fun getAllTickets(): Single<List<Ticket>>
//    fun getTicket(tickedId: String, completion: (Ticket?) -> Unit)
//    fun getUserTickets(userId: String, completion: (List<Ticket>) -> Unit)
//    fun getSavedTickets(userId: String, completion: (List<Ticket>) -> Unit)
//    fun getFavouriteTickets(userId: String, completion: (List<Ticket>) -> Unit)
//    fun searchTicket(ticket: Ticket, radius: Double, completion: (List<Ticket>) -> Unit)
//    fun uploadTicket(ticket: Ticket, completion: () -> Unit)// Send ticket into db
//    fun publishTicket(ticket: Ticket, completion: () -> Unit)// Make saved ticked published
//    fun updateTicket(newTicket: Ticket, completion: () -> Unit)
//    fun deleteTicket(ticket: Ticket, completion: () -> Unit)
//    fun doTicketFavourite(ticket: Ticket, completion: () -> Unit)
//    fun undoTicketFavourite(ticket: Ticket, completion: () -> Unit)
//
//    fun uploadPhoto(file: File, completion: () -> Unit)

}
