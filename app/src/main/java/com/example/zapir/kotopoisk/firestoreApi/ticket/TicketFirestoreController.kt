package com.example.zapir.kotopoisk.firestoreApi.ticket

import android.net.Uri
import com.example.zapir.kotopoisk.common.*
import com.example.zapir.kotopoisk.model.Ticket
import com.fernandocejas.arrow.optional.Optional
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import org.slf4j.LoggerFactory
import java.io.File

class TicketFirestoreController : TicketFirestoreInterface {

    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun getAllTickets(): Single<List<Ticket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .whereEqualTo("isPublished", true)
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(Ticket::class.java) }
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(tickets)
                    }
                    .addOnFailureListener {
                        logger.error("Error getting tickets: $it")
                        emitter.onError(getTicketsListExceptionApi())
                    }
        }
    }

    override fun getTicket(tickedId: String): Single<Optional<Ticket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .document(tickedId)
                    .get()
                    .addOnSuccessListener {
                        logger.info("Get ticket is successful")
                        if (!it.exists()) {
                            emitter.onSuccess(Optional.of(null))
                            return@addOnSuccessListener
                        }
                        val ticket = it.toObject(Ticket::class.java)
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        if (ticket == null) {
                            emitter.onError(SerializationExceptionApi())
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Optional.of(ticket))
                    }
                    .addOnFailureListener {
                        logger.error("Error getting ticket: $it")
                        emitter.onError(getTicketException())
                    }
        }

    }

    override fun getUserTickets(userId: String): Single<List<Ticket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .whereEqualTo("finderId", userId)
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(Ticket::class.java) }
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(tickets)
                    }
                    .addOnFailureListener {
                        logger.error("Error getting user ticket: $it")
                        emitter.onError(getTicketsListExceptionApi())
                    }
        }
    }

    override fun getSavedTickets(userId: String): Single<List<Ticket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .whereEqualTo("isPublished", false)
                    .whereEqualTo("finderId", userId)
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(Ticket::class.java) }
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(tickets)
                    }
                    .addOnFailureListener {
                        logger.error("Error getting saved ticket: $it")
                        emitter.onError(getTicketsListExceptionApi())
                    }
        }
    }

    override fun getFavouriteTickets(userId: String): Single<List<Ticket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("favouriteTickets")
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(Ticket::class.java) }
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(tickets)
                    }
                    .addOnFailureListener {
                        logger.error("Error getting ticket favourite: $it")
                        emitter.onError(getTicketsListExceptionApi())
                    }
        }
    }

    override fun uploadTicket(ticket: Ticket): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .document(ticket.id)
                    .set(ticket)
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error uploading ticket: $it")
                        emitter.onError(updateTicketExceptionApi())
                    }
        }
    }

    override fun publishTicket(ticket: Ticket): Single<Unit> {
        ticket.isPublished = true
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets"
            ).document(ticket.id)
                    .set(ticket)
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error publishing ticket: $it")
                        emitter.onError(uploadTicketExceptionApi())
                    }
        }
    }

    override fun updateTicket(newTicket: Ticket): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .document(newTicket.id)
                    .set(newTicket)
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error updating ticket: $it")
                        emitter.onError(updateTicketExceptionApi())
                    }
        }
    }

    override fun deleteTicket(ticket: Ticket): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .document(ticket.id)
                    .delete()
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error deleting ticket: $it")
                        emitter.onError(deleteTicketExceptionApi())
                    }
        }
    }

    override fun makeTicketFavourite(ticket: Ticket): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("favouriteTickets")
                    .document(ticket.id)
                    .set(ticket)
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error publishing ticket: $it")
                        emitter.onError(ApiBaseException())
                    }
        }
    }

    override fun makeTicketUnFavourite(ticket: Ticket): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("favouriteTickets")
                    .document(ticket.id)
                    .delete()
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error publishing ticket: $it")
                        emitter.onError(ApiBaseException())
                    }
        }
    }

    override fun uploadPhoto(file: File): Single<String> {
        val newFile = Uri.fromFile(file)
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            val photoRef = storageRef.child("images/" + newFile.lastPathSegment)
            val uploadTask = photoRef.putFile(newFile)
            val uriTask = uploadTask
                    .continueWithTask {
                        if (!it.isSuccessful) {
                            emitter.onError(it.exception!!)
                        }
                        photoRef.downloadUrl
                    }
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(it.toString())
                    }
                    .addOnFailureListener {
                        logger.error("Error uploading photo: $it")
                        emitter.onError(ApiBaseException())
                    }
        }
    }
}