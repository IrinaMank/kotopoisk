package com.example.zapir.kotopoisk.firestoreApi.user

import com.example.zapir.kotopoisk.common.NonAuthorizedException
import com.example.zapir.kotopoisk.common.SerializationException
import com.example.zapir.kotopoisk.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import org.slf4j.LoggerFactory

class UserFirestoreController : UserFirestoreInterface {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun getUser(userId: String): Single<User> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener {
                        logger.info("Get user is successful")
                        val user = it.toObject(User::class.java)
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        if (user == null) {
                            emitter.onError(SerializationException())
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(user)
                    }
                    .addOnFailureListener {
                        logger.error("Error getting user: $it")
                        emitter.onError(it)
                        //NetworkProblemsException(it.toString())
                    }
        }
    }

    override fun getCurrentUser(): Single<User> {
        val myId = auth.uid
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            if (myId == null) {
                emitter.onError(NonAuthorizedException())
                return@create
            }
            db.collection("users")
                    .document(myId)
                    .get()
                    .addOnSuccessListener {
                        logger.info("Get user is successful")
                        val user = it.toObject(User::class.java)
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        if (user == null) {
                            emitter.onError(SerializationException())
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(user)
                    }
                    .addOnFailureListener {
                        logger.error("Error getting user: $it")
                        emitter.onError(it)
                        //NetworkProblemsException(it.toString())
                    }
        }

    }

    override fun isAuthorized(): Single<Boolean> {
        return Single.create { emitter ->
            val myId = auth.uid
            if (emitter.isDisposed) {
                return@create
            }
            if (myId == null) {
                emitter.onSuccess(false)
            } else {
                emitter.onSuccess(true)
            }
        }
    }

    override fun updateUser(user: User): Single<Unit> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("users")
                    .document(user.id)
                    .set(user)
                    .addOnSuccessListener {
                        logger.info("Get user is successful")
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Unit)
                    }
                    .addOnFailureListener {
                        logger.error("Error updating user: $it")
                        emitter.onError(it)
                        //NetworkProblemsException(it.toString())
                    }
        }
    }

    override fun logInWithGoogle(account: GoogleSignInAccount): Single<FirebaseUser> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth?.signInWithCredential(credential)
                    ?.addOnSuccessListener {
                        logger.info("Get user is successful")
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(auth.currentUser!!)
                    }
                    ?.addOnFailureListener {
                        logger.error("Error updating user: $it")
                        emitter.onError(it)
                    }
        }
    }

    override fun logOut(){
            auth.signOut()
    }


}