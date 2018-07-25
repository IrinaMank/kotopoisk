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
    fun getUserTickets(userId: String, completion: (List<Ticket>) -> Unit)
    fun getSavedTickets(userId: String, completion: (List<Ticket>) -> Unit)
    fun getFavouriteTickets(userId: String, completion: (List<Ticket>) -> Unit)
    //fun searchTicket(animal: Animal, completion: (List<Animal>) -> Unit)
    fun uploadTicket(ticket: Ticket, completion: () -> Unit)// Send ticket into db
    fun publishTicket(ticket: Ticket, completion: () -> Unit)// Make saved ticked published
    fun updateTicket(newTicket: Ticket, completion: () -> Unit)
    fun deleteTicket(ticket: Ticket, completion: () -> Unit)
    fun doTicketFavourite(ticket: Ticket, completion: () -> Unit)
    fun undoTicketFavourite(ticket: Ticket, completion: () -> Unit)

    //Animals
    fun getAllAnimals(completion: (List<Animal>) -> Unit)
    fun getAnimal(animalId: String, completion: (Animal?) -> Unit)
    fun saveAnimal(animal: Animal, completion: () -> Unit)

    //Photos
    fun uploadPhoto()


}