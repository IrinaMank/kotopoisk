package com.example.zapir.kotopoisk.firestoreApi

import com.example.zapir.kotopoisk.model.Ticket
import com.example.zapir.kotopoisk.model.User
import com.google.firebase.firestore.FirebaseFirestore


interface FirestoreInterface {

    fun getUser(userId: String, completion: (User?) -> Unit)

    fun getAllTickets(completion: (List<Ticket>) -> Unit)
    //fun getTicket(tickedId: String, completion: (Ticket) -> Unit)

}