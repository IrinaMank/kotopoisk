package com.example.zapir.kotopoisk.firestoreApi

import com.example.zapir.kotopoisk.common.NetworkProblemsException
import com.example.zapir.kotopoisk.common.SerializationException
import com.example.zapir.kotopoisk.model.Animal
import com.example.zapir.kotopoisk.model.Ticket
import com.example.zapir.kotopoisk.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.slf4j.LoggerFactory
import com.google.firebase.storage.FirebaseStorage




class FirestoreController : FirestoreInterface {
    override fun uploadPhoto() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
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
                            logger.info("Get me is successful")
                            completion(me)
                        } else {
                            logger.error("Error getting me: Serialization")
                            throw SerializationException()
                        }
                    } else {
                        logger.error("Error getting me: No such element")
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
                    logger.info("Get user is successful")
                    val user = document.toObject(User::class.java)
                    completion(user)
                } else {
                    logger.error("Error getting user: No such element")
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
                logger.info("Get tickets is successful")
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
                logger.info("Update me is successful")
                completion()
            } else {
                logger.error("Error updating me: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun getTicket(tickedId: String, completion: (Ticket?) -> Unit) {
        db.collection("tickets").document(tickedId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result
                if (document.exists()) {
                    logger.info("Get ticket is successful")
                    val ticket = document.toObject(Ticket::class.java)
                    completion(ticket)
                } else {
                    logger.error("Error getting ticket: No such element")
                    throw NoSuchElementException()
                }
            } else {
                logger.error("Error getting ticket: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun getUserTickets(userId: String, completion: (List<Ticket>) -> Unit) {
        val tickets = mutableListOf<Ticket>()
        db.collection("tickets").whereEqualTo("finderId", userId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                logger.info("Get user tickets is successful")
                for (document in it.result) {
                    tickets.add(document.toObject(Ticket::class.java))
                }
                completion(tickets)
            } else {
                logger.error("Error getting user tickets: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun getSavedTickets(userId: String, completion: (List<Ticket>) -> Unit) {
        val tickets = mutableListOf<Ticket>()
        db.collection("tickets").whereEqualTo("isPublished", false).whereEqualTo("finderId", userId)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        logger.info("Get saved tickets is successful")
                        for (document in it.result) {
                            tickets.add(document.toObject(Ticket::class.java))
                        }
                        completion(tickets)
                    } else {
                        logger.error("Error getting saved tickets: ${it.exception}")
                        NetworkProblemsException(it.exception.toString())
                    }
                }
    }

    override fun getFavouriteTickets(userId: String, completion: (List<Ticket>) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun uploadTicket(ticket: Ticket, completion: () -> Unit) {
        db.collection("tickets").document(ticket.id).set(ticket).addOnCompleteListener {
            if (it.isSuccessful) {
                logger.info("Save ticket is successful")
                completion()
            } else {
                logger.error("Error saving ticket: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun publishTicket(ticket: Ticket, completion: () -> Unit) {
        db.collection("tickets").document(ticket.id).update("isPublished", true)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        logger.info("Publish ticket is successful")
                        completion()
                    } else {
                        logger.error("Error publishing ticket: ${it.exception}")
                        NetworkProblemsException(it.exception.toString())
                    }
                }
    }

    override fun updateTicket(newTicket: Ticket, completion: () -> Unit) {
        db.collection("tickets").document(newTicket.id).set(newTicket).addOnCompleteListener {
            if (it.isSuccessful) {
                logger.info("Update ticket is successful")
                completion()
            } else {
                logger.error("Error updating ticket: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun deleteTicket(ticket: Ticket, completion: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doTicketFavourite(ticket: Ticket, completion: () -> Unit) {
        //db.collection("tickets").
    }

    override fun undoTicketFavourite(ticket: Ticket, completion: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllAnimals(completion: (List<Animal>) -> Unit) {
        val animals = mutableListOf<Animal>()
        db.collection("animals").get().addOnCompleteListener {
            if (it.isSuccessful) {
                logger.info("Get animals is successful")
                for (document in it.result) {
                    animals.add(document.toObject(Animal::class.java))
                }
                completion(animals)
            } else {
                logger.error("Error getting animals: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun getAnimal(animalId: String, completion: (Animal?) -> Unit) {
        db.collection("animals").document(animalId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result
                if (document.exists()) {
                    logger.info("Get animal is successful")
                    val animal = document.toObject(Animal::class.java)
                    completion(animal)
                } else {
                    logger.error("Error getting animal: No such element")
                    throw NoSuchElementException()
                }
            } else {
                logger.error("Error getting animal: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

    override fun saveAnimal(animal: Animal, completion: () -> Unit) {
        db.collection("animals").document(animal.id).set(animal).addOnCompleteListener {
            if (it.isSuccessful) {
                logger.info("Save animal is successful")
                completion()
            } else {
                logger.error("Error saving animal: ${it.exception}")
                NetworkProblemsException(it.exception.toString())
            }
        }
    }

}
