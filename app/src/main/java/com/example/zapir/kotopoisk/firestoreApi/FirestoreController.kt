package com.example.zapir.kotopoisk.firestoreApi

import com.example.zapir.kotopoisk.model.User
import com.google.firebase.firestore.FirebaseFirestore
import org.slf4j.LoggerFactory
import java.nio.file.Files.exists
import com.example.zapir.kotopoisk.common.ElementAlreadyExistsException
import com.example.zapir.kotopoisk.common.NetworkProblemsException
import com.example.zapir.kotopoisk.model.Ticket
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot





class FirestoreController: FirestoreInterface {

    private val db =  FirebaseFirestore.getInstance()
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun getUser(userId: String, completion: (User?) -> Unit) {
        db.collection("users").document(userId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    completion(user)
                } else {
                    throw NoSuchElementException()
                }
            } else {
                logger.error("Error getting user: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun getAllTickets(completion: (List<Ticket>) -> Unit) {
        var tickets =  mutableListOf<Ticket>()
        db.collection("tickets").whereEqualTo("isPublished", true).get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result) {
                    tickets.add(document.toObject(Ticket::class.java))
                }
                completion(tickets)
            } else {
                logger.error("Error getting tickets: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }


}