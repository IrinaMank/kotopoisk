package com.example.zapir.kotopoisk.firestoreApi.user

import com.example.zapir.kotopoisk.common.NonAuthorizedExceptionApi
import com.example.zapir.kotopoisk.common.SerializationExceptionApi
import com.example.zapir.kotopoisk.common.getUserException
import com.example.zapir.kotopoisk.common.updateUserExceptionApi
import com.example.zapir.kotopoisk.model.User
import com.fernandocejas.arrow.optional.Optional
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import org.slf4j.LoggerFactory

class UserFirestoreController : UserFirestoreInterface {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    override fun getUser(userId: String): Single<Optional<User>> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener {
                        logger.info("Get user is successful")
                        if (!it.exists()) {
                            emitter.onSuccess(Optional.of(null))
                            return@addOnSuccessListener
                        }
                        val user = it.toObject(User::class.java)
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        if (user == null) {
                            emitter.onError(SerializationExceptionApi())
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(Optional.of(user))
                    }
                    .addOnFailureListener {
                        logger.error("Error getting user: $it")
                        emitter.onError(getUserException())
                    }
        }
    }

    override fun getCurrentUser(): Single<User> {
        return Single.create { emitter ->
            if (emitter.isDisposed) {
                return@create
            }
            val firebaseUser = auth.currentUser
            if (firebaseUser == null) {
                emitter.onError(NonAuthorizedExceptionApi())
                return@create
            }
            db.collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .addOnSuccessListener {
                        logger.info("Get user is successful")
                        val user = it.toObject(User::class.java)
                        if (emitter.isDisposed) {
                            return@addOnSuccessListener
                        }
                        if (user == null) {
                            emitter.onError(SerializationExceptionApi())
                            return@addOnSuccessListener
                        }
                        emitter.onSuccess(user)
                    }
                    .addOnFailureListener {
                        logger.error("Error getting user: $it")
                        emitter.onError(getUserException())
                    }
        }

    }

    override fun registerOrUpdateUser(user: User): Single<Unit> {
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
                        emitter.onError(updateUserExceptionApi())
                    }
                    .addOnCanceledListener {
                        emitter.onError(updateUserExceptionApi())
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

    override fun logInWithGoogle(account: GoogleSignInAccount): Single<User> {
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
                        val user = User(id = it.user.uid)
                        emitter.onSuccess(user)
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