package com.example.zapir.kotopoisk.firestoreApi.user

import com.example.zapir.kotopoisk.common.NetworkProblemsException
import com.example.zapir.kotopoisk.common.SerializationException
import com.example.zapir.kotopoisk.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.slf4j.LoggerFactory

class UserFirestoreController: UserFirestoreInterface {
    override fun logOut(completion: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logIn(completion: () -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun getMe(completion: (User) -> Unit) {
        val myId = auth.uid
        if (myId != null) {
            db.collection("users").document(myId).get()
                    .addOnSuccessListener {
                        if (it.exists()) {
                            val me = it.toObject(User::class.java)
                            if (me != null) {
                                logger.info("Get me is successful")
                                completion(me)
                            } else {
                                logger.error("Error getting me: Serialization")
                                throw SerializationException()
                            }
                        }
                    }
                    .addOnFailureListener {
                        logger.error("Error getting me: No such element")
                        throw NoSuchElementException()
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

}