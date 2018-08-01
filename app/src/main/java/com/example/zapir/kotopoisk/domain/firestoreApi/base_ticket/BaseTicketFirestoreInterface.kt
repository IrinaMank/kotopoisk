package com.example.zapir.kotopoisk.domain.firestoreApi.base_ticket

import android.net.Uri
import com.example.zapir.kotopoisk.data.model.FavoriteTicket
import com.example.zapir.kotopoisk.data.model.Photo
import com.fernandocejas.arrow.optional.Optional
import io.reactivex.Single
import java.io.File

interface BaseTicketFirestoreInterface<T> {

    fun getAllTickets(): Single<List<T>>
    fun getTicket(tickedId: String): Single<Optional<T>>
    fun getUserTickets(userId: String): Single<List<T>>
    fun getSavedTickets(userId: String): Single<List<T>>
    fun getFavouriteTickets(userId: String): Single<List<FavoriteTicket>>
    fun searchTicket(ticket: T): Single<List<T>>
    fun uploadTicket(ticket: T): Single<Unit>//save unpublished ticket

    fun publishTicket(ticket: T): Single<Unit>//make saved ticket published or publish new
    // ticket
    fun updateTicket(newTicket: T): Single<Unit>

    fun deleteTicket(ticket: T): Single<Unit>
    fun makeTicketFavourite(ticket: T): Single<Unit>
    fun makeTicketUnFavourite(ticket: T): Single<Unit>
    fun isTicketFavorite(ticketId: String, userId: String): Single<Boolean>
    fun ticketIsFound(ticket: T): Single<Unit>

    fun uploadPhoto(file: Uri): Single<String>//return URI of upload file
    fun getPhoto(ticketId: String): Single<Photo>


}
