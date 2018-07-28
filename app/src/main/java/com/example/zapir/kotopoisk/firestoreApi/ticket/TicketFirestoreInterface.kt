package com.example.zapir.kotopoisk.firestoreApi.ticket

import com.example.zapir.kotopoisk.model.Photo
import com.example.zapir.kotopoisk.model.Ticket
import com.fernandocejas.arrow.optional.Optional
import io.reactivex.Single
import java.io.File

interface TicketFirestoreInterface {

    fun getAllTickets(): Single<List<Ticket>>
    fun getTicket(tickedId: String): Single<Optional<Ticket>>
    fun getUserTickets(userId: String): Single<List<Ticket>>
    fun getSavedTickets(userId: String): Single<List<Ticket>>
    fun getFavouriteTickets(userId: String): Single<List<Ticket>>
//    fun searchTicket(ticket: Ticket, radius: Double, completion: (List<Ticket>) -> Unit)
    fun uploadTicket(ticket: Ticket): Single<Unit>//save unpublished ticket
    fun publishTicket(ticket: Ticket): Single<Unit>//make saved ticket published or publish new
    // ticket
    fun updateTicket(newTicket: Ticket): Single<Unit>
    fun deleteTicket(ticket: Ticket): Single<Unit>
    fun makeTicketFavourite(ticket: Ticket): Single<Unit>
    fun makeTicketUnFavourite(ticket: Ticket): Single<Unit>

    fun uploadPhoto(file: File, ticketId: String): Single<Unit>
    fun getPhoto(ticketId: String): Single<Photo>

}
