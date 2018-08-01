package com.example.zapir.kotopoisk.domain.firestoreApi.base_ticket

import android.net.Uri
import com.example.zapir.kotopoisk.data.exceptions.*
import com.example.zapir.kotopoisk.data.model.BaseTicket
import com.example.zapir.kotopoisk.data.model.FavoriteTicket
import com.example.zapir.kotopoisk.data.model.Photo
import com.fernandocejas.arrow.optional.Optional
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import org.slf4j.LoggerFactory

class BaseTicketFirestoreController : BaseTicketFirestoreInterface<BaseTicket> {

    override fun uploadPhotoBase(photo: Photo): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("photos")
                    .document(photo.id)
                    .set(photo)
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error uploading photo: $it")
                        emitter.onError(UpdateTicketExceptionApi())
                    }
        }    }

    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun getAllTickets(): Single<List<BaseTicket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .whereEqualTo("published", true)
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(BaseTicket::class.java) }
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


    override fun getTicket(tickedId: String): Single<Optional<BaseTicket>> {
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
                        val ticket = it.toObject(BaseTicket::class.java)
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

    override fun getUserTickets(userId: String): Single<List<BaseTicket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .whereEqualTo("finderId", userId)
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(BaseTicket::class.java) }
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

    override fun getSavedTickets(userId: String): Single<List<BaseTicket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("tickets")
                    .whereEqualTo("isPublished", false)
                    .whereEqualTo("finderId", userId)
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(BaseTicket::class.java) }
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

    override fun getFavouriteTickets(userId: String): Single<List<FavoriteTicket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("favouriteTickets")
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(FavoriteTicket::class.java) }
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


    override fun uploadTicket(ticket: BaseTicket): Single<Unit> {
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

    override fun publishTicket(ticket: BaseTicket): Single<Unit> {
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

    override fun updateTicket(newTicket: BaseTicket): Single<Unit> {
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

    override fun deleteTicket(ticket: BaseTicket): Single<Unit> {
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

    override fun makeTicketFavourite(ticket: BaseTicket): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            val ticketFavor = FavoriteTicket(userId = ticket.finderId, ticketId = ticket.id)
            db.collection("favouriteTickets")
                    .add(ticketFavor)
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

    override fun makeTicketUnFavourite(ticket: BaseTicket): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("favouriteTickets")
                    .whereEqualTo("ticketId", ticket.id)
                    .get()
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        if (!it.isEmpty) {
                            it.forEach { it.reference.delete() }
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error publishing ticket: $it")
                        emitter.onError(ApiBaseException())
                    }
        }

    }

    override fun uploadPhoto(file: Uri): Single<String> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            val photoRef = storageRef.child("images/" + file.lastPathSegment)
            val uploadTask = photoRef.putFile(file)
            uploadTask
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
                        if (!it.documents.isEmpty()) {
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
                        } else {
                            emitter.onSuccess(Photo())//add empty photo
                        }
                    }
                    .addOnFailureListener {
                        logger.error("Error getting ticket: $it")
                        emitter.onError(GetTicketException())
                    }
        }
    }

    override fun searchTicket(ticket: BaseTicket): Single<List<BaseTicket>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            var query = db.collection("tickets")
                    .whereEqualTo("type", ticket.type)
                    .whereEqualTo("color", ticket.color)
                    .whereEqualTo("size", ticket.size)
                    .whereEqualTo("hasCollar", ticket.hasCollar)
                    .whereEqualTo("furLength", ticket.furLength)
                    .whereEqualTo("published", true)
            if (ticket.breed != 0) {
                query = query.whereEqualTo("breed", ticket.breed)
            }
            query.get()
                    .addOnSuccessListener {
                        logger.info("Get tickets is successful")
                        val tickets = it.map { document -> document.toObject(BaseTicket::class.java) }
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(tickets)

                    }
                    .addOnFailureListener {
                        emitter.onError(GetTicketsListExceptionApi())
                    }
        }
    }

    override fun ticketIsFound(ticket: BaseTicket): Single<Unit> {
        ticket.isFound = true
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
                        logger.error("Error updating ticket: $it")
                        emitter.onError(UpdateTicketExceptionApi())
                    }
        }
    }

    override fun isTicketFavorite(ticketId: String, userId: String): Single<Boolean> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("favouriteTickets")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("ticketId", ticketId)
                    .get()
                    .addOnSuccessListener {
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        if (!it.isEmpty) {
                            emitter.onSuccess(true)
                        } else {
                            emitter.onSuccess(false)
                        }
                    }
                    .addOnFailureListener {
                        logger.error("Error uploading ticket: $it")
                        emitter.onError(UpdateTicketExceptionApi())
                    }
        }
    }



}
