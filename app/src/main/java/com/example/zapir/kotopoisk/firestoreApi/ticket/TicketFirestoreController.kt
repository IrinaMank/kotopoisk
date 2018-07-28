package com.example.zapir.kotopoisk.firestoreApi.ticket

import android.net.Uri
import android.support.v4.content.ContextCompat.getDrawable
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.common.exceptions.*
import com.example.zapir.kotopoisk.model.Photo
import com.example.zapir.kotopoisk.model.Ticket
import com.fernandocejas.arrow.optional.Optional
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import org.slf4j.LoggerFactory
import java.io.File

class TicketFirestoreController : TicketFirestoreInterface {

    override fun getPhoto(ticketId: String): Single<Photo> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("photos")
                    .whereEqualTo("ticketId", ticketId)
                    .get()
                    .addOnSuccessListener {
                        logger.info("Get ticket is successful")
                        val photo = it.documents[0].toObject(Photo::class.java)//it's really bad
                        // but now it works :)
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        if (photo == null) {
                            emitter.onError(SerializationExceptionApi())
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(photo)
                    }
                    .addOnFailureListener {
                        logger.error("Error getting ticket: $it")
                        emitter.onError(GetTicketException())
                    }
        }
    }

    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)
    private val auth = FirebaseAuth.getInstance()

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
                        emitter.onError(GetTicketsListExceptionApi())
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
                            emitter.onSuccess(Optional.fromNullable(null))
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
                        emitter.onError(GetTicketException())
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
                        emitter.onError(GetTicketsListExceptionApi())
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
                        emitter.onError(GetTicketsListExceptionApi())
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
                        emitter.onError(GetTicketsListExceptionApi())
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
                        emitter.onError(UpdateTicketExceptionApi())
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
                        emitter.onError(UploadTicketExceptionApi())
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
                        emitter.onError(UpdateTicketExceptionApi())
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
                        emitter.onError(DeleteTicketExceptionApi())
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

    private fun uploadFile(file: File): Single<String> {
        val newFile = Uri.fromFile(file)
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            val photoRef = storageRef.child("images/" + newFile.lastPathSegment)
            val uploadTask = photoRef.putFile(newFile)
            val url = uploadTask
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
                    }.result
        }
    }
    override fun uploadPhoto(file: File, ticketId: String): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            uploadFile(file).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        val photo = Photo(url = it, userId = auth.uid.toString(), ticketId =
                        ticketId)
                        db.collection("photos"
                        ).document(photo.id)
                                .set(photo)
                                .addOnSuccessListener {
                                    if (emitter.isDisposed) {
                                        return@addOnSuccessListener
                                    }
                                    emitter.onSuccess(Unit)
                                }
                                .addOnFailureListener {
                                    logger.error("Error publishing photo: $it")
                                    emitter.onError(UploadTicketExceptionApi())//ToDo: not ticket
                                }

                    },
                    {
                        emitter.onError(it)
                    }
            )
        }
    }


}