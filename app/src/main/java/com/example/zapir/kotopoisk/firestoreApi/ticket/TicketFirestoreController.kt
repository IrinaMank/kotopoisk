package com.example.zapir.kotopoisk.firestoreApi.ticket

import android.net.Uri
import com.example.zapir.kotopoisk.common.NetworkProblemsException
import com.example.zapir.kotopoisk.model.Ticket
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Observable
import io.reactivex.Single
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class TicketFirestoreController: TicketFirestoreInterface {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun getAllTickets(): Single<List<Ticket>> {
        return Single.create { emitter ->
            db.collection("tickets")
                    .whereEqualTo("isPublished", true)
                    .get()
                    .addOnSuccessListener {
                        val tickets = it.map { document -> document.toObject(Ticket::class.java) }
                        if (!emitter.isDisposed) {
                            emitter.onSuccess(tickets)
                        } else {
                            emitter.onError(NetworkProblemsException("Error while mapping"))
                        }
                    }
                .addOnFailureListener {
                    logger.error("Error getting ticket favourite: $it")
                    NetworkProblemsException(it.toString())
                }
        }
    }

}

//                .addOnCompleteListener {
//            if (it.isSuccessful) {
//                logger.info("Get tickets is successful")
//                for (document in it.result) {
//                    tickets.add(document.toObject(Ticket::class.java))
//                }
//                completion(tickets)
//            } else {
//                logger.error("Error getting tickets: ${it.exception}")
//                NetworkProblemsException(it.exception.toString())
//            }
//        }

//    override fun searchTicket(ticket: Ticket, radius: Int, completion: (List<Ticket>) -> Unit) {
//        db.collection("tickets").whereEqualTo("")
//    }



//    override fun getAllTickets(completion: (List<Ticket>) -> Unit) {
//        val tickets = mutableListOf<Ticket>()
//        db.collection("tickets").whereEqualTo("isPublished", true).get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                logger.info("Get tickets is successful")
//                for (document in it.result) {
//                    tickets.add(document.toObject(Ticket::class.java))
//                }
//                completion(tickets)
//            } else {
//                logger.error("Error getting tickets: ${it.exception}")
//                NetworkProblemsException(it.exception.toString())
//            }
//        }
//    }
//
//
//    override fun getTicket(tickedId: String, completion: (Ticket?) -> Unit) {
//        db.collection("tickets").document(tickedId).get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                val document = it.result
//                if (document.exists()) {
//                    logger.info("Get ticket is successful")
//                    val ticket = document.toObject(Ticket::class.java)
//                    completion(ticket)
//                } else {
//                    logger.error("Error getting ticket: No such element")
//                    throw NoSuchElementException()
//                }
//            } else {
//                logger.error("Error getting ticket: ${it.exception}")
//                NetworkProblemsException(it.exception.toString())
//            }
//        }
//    }
//
//    override fun getUserTickets(userId: String, completion: (List<Ticket>) -> Unit) {
//        val tickets = mutableListOf<Ticket>()
//        db.collection("tickets").whereEqualTo("finderId", userId).get().addOnCompleteListener {
//            if (it.isSuccessful) {
//                logger.info("Get user tickets is successful")
//                for (document in it.result) {
//                    tickets.add(document.toObject(Ticket::class.java))
//                }
//                completion(tickets)
//            } else {
//                logger.error("Error getting user tickets: ${it.exception}")
//                NetworkProblemsException(it.exception.toString())
//            }
//        }
//    }
//
//    override fun getSavedTickets(userId: String, completion: (List<Ticket>) -> Unit) {
//        val tickets = mutableListOf<Ticket>()
//        db.collection("tickets").whereEqualTo("isPublished", false).whereEqualTo("finderId", userId)
//                .get()
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        logger.info("Get saved tickets is successful")
//                        for (document in it.result) {
//                            tickets.add(document.toObject(Ticket::class.java))
//                        }
//                        completion(tickets)
//                    } else {
//                        logger.error("Error getting saved tickets: ${it.exception}")
//                        NetworkProblemsException(it.exception.toString())
//                    }
//                }
//    }
//
//    override fun getFavouriteTickets(userId: String, completion: (List<Ticket>) -> Unit) {
//        val tickets = mutableListOf<Ticket>()
//        db.collection("favouriteTickets").get()
//                .addOnSuccessListener {
//                    for (document in it) {
//                        tickets.add(document.toObject(Ticket::class.java))
//                    }
//                    completion(tickets)
//                }
//                .addOnFailureListener {
//                    logger.error("Error getting ticket favourite: $it")
//                    NetworkProblemsException(it.toString())
//                }
//    }
//
//    override fun uploadTicket(ticket: Ticket, completion: () -> Unit) {
//        db.collection("tickets").document(ticket.id).set(ticket).addOnCompleteListener {
//            if (it.isSuccessful) {
//                logger.info("Save ticket is successful")
//                completion()
//            } else {
//                logger.error("Error saving ticket: ${it.exception}")
//                NetworkProblemsException(it.exception.toString())
//            }
//        }
//    }
//
//    override fun publishTicket(ticket: Ticket, completion: () -> Unit) {
//        ticket.isPublished = true
//        db.collection("tickets").document(ticket.id).set(ticket)
//                .addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        logger.info("Publish ticket is successful")
//                        completion()
//                    } else {
//                        logger.error("Error publishing ticket: ${it.exception}")
//                        NetworkProblemsException(it.exception.toString())
//                    }
//                }
//    }
//
//    override fun updateTicket(newTicket: Ticket, completion: () -> Unit) {
//        db.collection("tickets").document(newTicket.id).set(newTicket).addOnCompleteListener {
//            if (it.isSuccessful) {
//                logger.info("Update ticket is successful")
//                completion()
//            } else {
//                logger.error("Error updating ticket: ${it.exception}")
//                NetworkProblemsException(it.exception.toString())
//            }
//        }
//    }
//
//    override fun deleteTicket(ticket: Ticket, completion: () -> Unit) {
//        db.collection("tickets").document(ticket.id).delete()
//                .addOnSuccessListener { completion() }
//                .addOnFailureListener {
//                    logger.error("Error deleting ticket: $it")
//                    NetworkProblemsException(it.toString())
//                }
//    }
//
//    override fun doTicketFavourite(ticket: Ticket, completion: () -> Unit) {
//        db.collection("favouriteTickets").document(ticket.id).set(ticket)
//                .addOnSuccessListener { completion() }
//                .addOnFailureListener {
//                    logger.error("Error making ticket favourite: $it")
//                    NetworkProblemsException(it.toString())
//                }
//    }
//
//    override fun undoTicketFavourite(ticket: Ticket, completion: () -> Unit) {
//        db.collection("favouriteTickets").document(ticket.id).delete()
//                .addOnSuccessListener { completion() }
//                .addOnFailureListener {
//                    logger.error("Error making ticket unfavourite: $it")
//                    NetworkProblemsException(it.toString())
//                }
//    }
//
//    override fun uploadPhoto(file: File, completion: () -> Unit) {
//        val newFile = Uri.fromFile(file)
//        val photoRef = storageRef.child("images/" + newFile.lastPathSegment)
//        photoRef.putFile(newFile)
//                .addOnSuccessListener { completion() }
//                .addOnFailureListener {
//                    logger.error("Error getting me: ${it}")
//                    throw NetworkProblemsException(it.toString())
//                }
//    }
//
//    private fun kilometersToLat(radius: Double, lat: Double, lng: Double) {
//        6371 * acos( cos(lat) * cos( lat ) * cos( lng ) - lng ) + sin( lat ) * sin( lat )
//    }
