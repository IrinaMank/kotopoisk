package com.example.zapir.kotopoisk.domain.firestoreApi.ticket

import android.net.Uri
import com.example.zapir.kotopoisk.data.model.BaseTicket
import com.example.zapir.kotopoisk.data.model.FavoriteTicket
import com.example.zapir.kotopoisk.data.model.Photo
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.domain.firestoreApi.base_ticket.BaseTicketFirestoreController
import com.example.zapir.kotopoisk.domain.firestoreApi.user.UserFirestoreController
import com.fernandocejas.arrow.optional.Optional
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class TicketFirestoreController : TicketFirestoreInterface {

    private val baseController = BaseTicketFirestoreController()
    private val userController = UserFirestoreController()
    private val auth = FirebaseAuth.getInstance()

    override fun getAllTickets(): Single<List<Ticket>> {
        return baseController.getAllTickets()
                .flatMapObservable { list: List<BaseTicket> -> Observable.fromIterable(list) }
                .flatMapSingle { ticket: BaseTicket -> convertToTicket(ticket) }
                .flatMapSingle { ticket: Ticket -> addPhotoToTicket(ticket) }
                .flatMapSingle { t: Ticket -> isFavorite(t, auth.uid.toString()) }
                .toList()
    }


    override fun getTicket(tickedId: String): Single<Optional<Ticket>> {
        return baseController.getTicket(tickedId)
                .flatMap { convertToTicket(it.get()) }
                .flatMap { addPhotoToTicket(it) }
                .flatMap { isFavorite(it, auth.uid.toString()) }
                .map { Optional.of(it) }
    }

    override fun getUserTickets(userId: String): Single<List<Ticket>> {
        return baseController.getUserTickets(userId)
                .flatMapObservable { list: List<BaseTicket> -> Observable.fromIterable(list) }
                .flatMapSingle { ticket: BaseTicket -> convertToTicket(ticket) }
                .flatMapSingle { ticket: Ticket -> addPhotoToTicket(ticket) }
                .flatMapSingle { t: Ticket -> isFavorite(t, auth.uid.toString()) }
                .toList()
    }

    override fun getSavedTickets(userId: String): Single<List<Ticket>> {
        return baseController.getSavedTickets(userId)
                .flatMapObservable { list: List<BaseTicket> -> Observable.fromIterable(list) }
                .flatMapSingle { ticket: BaseTicket -> convertToTicket(ticket) }
                .flatMapSingle { ticket: Ticket -> addPhotoToTicket(ticket) }
                .flatMapSingle { t: Ticket -> isFavorite(t, auth.uid.toString()) }
                .toList()
    }

    override fun getFavouriteTickets(userId: String): Single<List<Ticket>> {
        return baseController.getFavouriteTickets(userId)
                .flatMapObservable { list: List<FavoriteTicket> -> Observable.fromIterable(list) }
                .flatMapSingle { ticket: FavoriteTicket -> baseController.getTicket(ticket.ticketId) }
                .flatMapSingle { ticket: Optional<BaseTicket> -> convertToTicket(ticket.get()) }
                .flatMapSingle { ticket: Ticket -> addPhotoToTicket(ticket) }
                .flatMapSingle { t: Ticket -> isFavorite(t, auth.uid.toString()) }
                .toList()
    }


    override fun uploadTicket(ticket: Ticket): Completable {
        return baseController.uploadTicket(toBaseTicket(ticket))
                .concatWith(baseController.uploadPhotoBase(ticket.photo))
    }

    override fun publishTicket(ticket: Ticket): Completable {
        ticket.user.petCount += 1
        return userController.registerUser(ticket.user)
                .concatWith(baseController.publishTicket(toBaseTicket(ticket)))
                .concatWith(baseController.uploadPhotoBase(ticket.photo))
    }

    override fun updateTicket(newTicket: Ticket): Completable {
        return baseController.updateTicket(toBaseTicket(newTicket))
    }

    override fun deleteTicket(ticket: Ticket): Completable {
        ticket.user.petCount -= 1
        var query = baseController.deleteTicket(toBaseTicket(ticket))
                .concatWith(userController.registerUser(ticket.user))
        if (ticket.isFavorite) {
            query = query.concatWith(baseController.makeTicketUnFavourite(toBaseTicket(ticket),
                    auth.uid.toString()))
        }
        return query
    }

    override fun makeTicketFavourite(ticket: Ticket): Completable {
        ticket.isFavorite = true
        return baseController.makeTicketFavourite(toBaseTicket(ticket), auth.uid.toString())
    }

    override fun makeTicketUnFavourite(ticket: Ticket): Completable {
        ticket.isFavorite = false
        return baseController.makeTicketUnFavourite(toBaseTicket(ticket), auth.uid.toString())
    }

    override fun uploadPhoto(file: Uri): Single<String> {
        return baseController.uploadPhoto(file)
    }

    override fun getPhoto(ticketId: String): Single<Photo> {
        return baseController.getPhoto(ticketId)
    }

    override fun searchTicket(ticket: Ticket): Single<List<Ticket>> {
        return baseController.searchTicket(toBaseTicket(ticket))
                .flatMapObservable { list: List<BaseTicket> -> Observable.fromIterable(list) }
                .flatMapSingle { foundTicket: BaseTicket -> convertToTicket(foundTicket) }
                .flatMapSingle { foundTicket: Ticket -> addPhotoToTicket(foundTicket) }
                .flatMapSingle { t: Ticket -> isFavorite(t, auth.uid.toString()) }
                .toList()
    }


    private fun convertToTicket(baseTicket: BaseTicket): Single<Ticket> {
        val ticket = toTicket(baseTicket)
        return Single.create { emitter ->
            userController.getUser(baseTicket.finderId).timeout(5, TimeUnit
                    .SECONDS)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                            {
                                if (it.isPresent) {
                                    ticket.user = it.get()
                                }
                                emitter.onSuccess(ticket)

                            },
                            {
                                throw it
                            }
                    )
        }
    }

    override fun ticketIsFound(ticket: Ticket): Completable {
        ticket.user.foundPetCount += 1
        return baseController.ticketIsFound(toBaseTicket(ticket)).concatWith(userController
                .registerUser(ticket.user))
    }


    private fun addPhotoToTicket(ticket: Ticket): Single<Ticket> {
        return Single.create { emitter ->
            baseController.getPhoto(ticket.id).timeout(5, TimeUnit.SECONDS)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                            {
                                ticket.photo = it
                                emitter.onSuccess(ticket)
                            },
                            {
                                throw it
                            }
                    )
        }
    }

    private fun isFavorite(ticket: Ticket, userId: String): Single<Ticket> {
        return Single.create { emitter ->
            baseController.isTicketFavorite(ticket.id, userId).timeout(5, TimeUnit.SECONDS)
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                            {
                                ticket.isFavorite = it
                                emitter.onSuccess(ticket)
                            },
                            {
                                throw it
                            }
                    )
        }
    }

    private fun toTicket(baseTicket: BaseTicket) = Ticket(
            id = baseTicket.id,
            lat = baseTicket.lat,
            lng = baseTicket.lng,
            date = baseTicket.date,
            overview = baseTicket.overview,
            type = baseTicket.type,
            color = baseTicket.color,
            size = baseTicket.size,
            hasCollar = baseTicket.hasCollar,
            breed = baseTicket.breed,
            furLength = baseTicket.furLength,
            isFound = baseTicket.isFound,
            isPublished = baseTicket.isPublished
    )

    private fun toBaseTicket(ticket: Ticket): BaseTicket {
        return BaseTicket(
                id = ticket.id,
                lat = ticket.lat,
                lng = ticket.lng,
                date = ticket.date,
                overview = ticket.overview,
                type = ticket.type,
                color = ticket.color,
                size = ticket.size,
                hasCollar = ticket.hasCollar,
                breed = ticket.breed,
                furLength = ticket.furLength,
                isFound = ticket.isFound,
                isPublished = ticket.isPublished,
                finderId = ticket.user.id
        )
    }

    override fun isTicketFavorite(ticket: Ticket, userId: String): Single<Boolean> {
        return baseController.isTicketFavorite(ticket.id, userId)
    }

}
