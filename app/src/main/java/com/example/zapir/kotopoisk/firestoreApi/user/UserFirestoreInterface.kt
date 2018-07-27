package com.example.zapir.kotopoisk.firestoreApi.user

import com.example.zapir.kotopoisk.model.User
import com.fernandocejas.arrow.optional.Optional
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

interface UserFirestoreInterface {

    fun getUser(userId: String): Single<Optional<User>>
    fun getCurrentUser(): Single<User>
    fun isAuthorized(): Single<Boolean>
    fun registerOrUpdateUser(user: User): Single<Unit>
    fun logInWithGoogle(account: GoogleSignInAccount): Single<User>
    fun logOut()

}
