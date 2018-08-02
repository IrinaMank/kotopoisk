package com.example.zapir.kotopoisk.domain.firestoreApi.ticket

import android.net.Uri
import com.example.zapir.kotopoisk.data.model.Photo
import com.example.zapir.kotopoisk.data.model.Ticket
import com.fernandocejas.arrow.optional.Optional
import io.reactivex.Completable
import io.reactivex.Single

interface TicketFirestoreInterface {

    fun getAllTickets(): Single<List<Ticket>>
    fun getTicket(tickedId: String): Single<Optional<Ticket>>
    fun getUserTickets(userId: String): Single<List<Ticket>>
    fun getSavedTickets(userId: String): Single<List<Ticket>>
    fun getFavouriteTickets(userId: String): Single<List<Ticket>>
    fun searchTicket(ticket: Ticket): Single<List<Ticket>>
    fun uploadTicket(ticket: Ticket): Completable//save unpublished ticket

    fun publishTicket(ticket: Ticket): Completable//make saved ticket published or publish new
    // ticket
    fun updateTicket(newTicket: Ticket): Completable

    fun deleteTicket(ticket: Ticket): Completable
    fun makeTicketFavourite(ticket: Ticket): Completable
    fun makeTicketUnFavourite(ticket: Ticket): Completable
    fun isTicketFavorite(ticket: Ticket, userId: String): Single<Boolean>
    fun ticketIsFound(ticket: Ticket): Completable

    fun uploadPhoto(file: Uri): Single<String>//return URI of upload file
    fun getPhoto(ticketId: String): Single<Photo>
}
