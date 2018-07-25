package com.example.zapir.kotopoisk.firestoreApi

import com.example.zapir.kotopoisk.model.Animal
import com.example.zapir.kotopoisk.model.Ticket
import com.example.zapir.kotopoisk.model.User
import com.google.firebase.firestore.FirebaseFirestore


interface FirestoreInterface {

    //Users
    fun getUser(userId: String, completion: (User?) -> Unit)
    fun getMe(completion: (User) -> Unit = {})
    fun updateMe(user: User, completion: () -> Unit)

    //Tickets
    fun getAllTickets(completion: (List<Ticket>) -> Unit)
    fun getTicket(tickedId: String, completion: (Ticket?) -> Unit)
    fun getUserTickets(userId: String, completion: (Ticket) -> Unit)
    fun getSavedTickets(userId: String, completion: (List<Ticket>) -> Unit)
    fun getFavouriteTickets(userId: String, completion: (List<Ticket>) -> Unit)
    //fun searchTicket(animal: Animal, completion: (List<Animal>) -> Unit)
    fun saveTicket(ticket: Ticket, completion: (Ticket) -> Unit)
    fun publishTicket(ticket: Ticket, completion: (Ticket) -> Unit)
    fun updateTicket(newTicket: Ticket, completion: (Ticket) -> Unit)


    fun getAllAnimals(completion: (List<Animal>) -> Unit)
    fun getAnimal(animalId: String, completion: (Animal?) -> Unit)
    fun saveAnimal(animal: Animal, completion: (Animal) -> Unit)



}