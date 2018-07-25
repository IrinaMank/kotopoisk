package com.example.zapir.kotopoisk.firestoreApi

import com.example.zapir.kotopoisk.common.NetworkProblemsException
import com.example.zapir.kotopoisk.common.SerializationException
import com.example.zapir.kotopoisk.model.Animal
import com.example.zapir.kotopoisk.model.Ticket
import com.example.zapir.kotopoisk.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.slf4j.LoggerFactory


class FirestoreController : FirestoreInterface {


    companion object {
        private const val AUTH_ID = "979610091334-rm9dte6qqcsb9jc167fqcqh9f7ucn9cc.apps" +
                ".googleusercontent.com"
        private const val RC_SIGN_IN = 9001
    }

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun getMe(completion: (User) -> Unit) {
        val myId = auth.uid
        if (myId != null) {
            db.collection("users").document(myId).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document.exists()) {
                        val me = document.toObject(User::class.java)
                        if (me != null) {
                            completion(me)
                        } else {
                            throw SerializationException()
                        }
                    } else {
                        throw NoSuchElementException()
                    }
                } else {
                    logger.error("Error getting me: ${it.exception}")
                    throw NetworkProblemsException(it.exception.toString())
                }
            }
        }
    }

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
        val tickets = mutableListOf<Ticket>()
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

    override fun updateMe(user: User, completion: () -> Unit) {
        db.collection("users").document(user.id).set(user).addOnCompleteListener {
            if (it.isSuccessful) {
                completion()
            } else {
                logger.error("Error getting tickets: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun getTicket(tickedId: String, completion: (Ticket?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserTickets(userId: String, completion: (Ticket) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSavedTickets(userId: String, completion: (List<Ticket>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFavouriteTickets(userId: String, completion: (List<Ticket>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveTicket(ticket: Ticket, completion: (Ticket) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun publishTicket(ticket: Ticket, completion: (Ticket) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTicket(newTicket: Ticket, completion: (Ticket) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllAnimals(completion: (List<Animal>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAnimal(animalId: String, completion: (Animal?) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAnimal(animal: Animal, completion: (Animal) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
