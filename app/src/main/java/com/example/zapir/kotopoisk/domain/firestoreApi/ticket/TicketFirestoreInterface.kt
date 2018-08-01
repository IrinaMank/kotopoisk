package com.example.zapir.kotopoisk.domain.firestoreApi.ticket

import android.net.Uri
import com.example.zapir.kotopoisk.data.model.Photo
import com.example.zapir.kotopoisk.data.model.Ticket
import com.fernandocejas.arrow.optional.Optional
import io.reactivex.Flowable
import io.reactivex.Single
import java.io.File

interface TicketFirestoreInterface {

    fun getAllTickets(): Single<List<Ticket>>
    fun getTicket(tickedId: String): Single<Optional<Ticket>>
    fun getUserTickets(userId: String): Single<List<Ticket>>
    fun getSavedTickets(userId: String): Single<List<Ticket>>
    fun getFavouriteTickets(userId: String): Single<List<Ticket>>
    fun searchTicket(ticket: Ticket): Single<List<Ticket>>
    fun uploadTicket(ticket: Ticket): Single<Unit>//save unpublished ticket

    fun publishTicket(ticket: Ticket): Flowable<Unit>//make saved ticket published or publish new
    // ticket
    fun updateTicket(newTicket: Ticket): Single<Unit>

    fun deleteTicket(ticket: Ticket): Flowable<Unit>
    fun makeTicketFavourite(ticket: Ticket): Single<Unit>
    fun makeTicketUnFavourite(ticket: Ticket): Single<Unit>
    fun isTicketFavorite(ticket: Ticket, userId: String): Single<Boolean>
    fun ticketIsFound(ticket: Ticket): Single<Unit>

    fun uploadPhoto(file: Uri): Single<String>//return URI of upload file
    fun getPhoto(ticketId: String): Single<Photo>
}
