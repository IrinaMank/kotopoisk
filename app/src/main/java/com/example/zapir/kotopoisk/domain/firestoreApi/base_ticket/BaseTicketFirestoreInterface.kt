package com.example.zapir.kotopoisk.domain.firestoreApi.base_ticket

import android.net.Uri
import com.example.zapir.kotopoisk.data.model.FavoriteTicket
import com.example.zapir.kotopoisk.data.model.Photo
import com.fernandocejas.arrow.optional.Optional
import io.reactivex.Completable
import io.reactivex.Single

interface BaseTicketFirestoreInterface<T> {

    fun getAllTickets(): Single<List<T>>
    fun getTicket(tickedId: String): Single<Optional<T>>
    fun getUserTickets(userId: String): Single<List<T>>
    fun getSavedTickets(userId: String): Single<List<T>>
    fun getFavouriteTickets(userId: String): Single<List<FavoriteTicket>>
    fun searchTicket(ticket: T): Single<List<T>>
    fun uploadTicket(ticket: T): Completable//save unpublished ticket

    fun publishTicket(ticket: T): Completable//make saved ticket published or publish new
    // ticket
    fun updateTicket(newTicket: T): Completable

    fun deleteTicket(ticket: T): Completable
    fun makeTicketFavourite(ticket: T, userId: String): Completable
    fun makeTicketUnFavourite(ticket: T, userId: String): Completable
    fun isTicketFavorite(ticketId: String, userId: String): Single<Boolean>
    fun ticketIsFound(ticket: T): Completable

    fun uploadPhoto(file: Uri): Single<String>//return URI of upload file
    fun uploadPhotoBase(photo: Photo): Completable
    fun getPhoto(ticketId: String): Single<Photo>


}
