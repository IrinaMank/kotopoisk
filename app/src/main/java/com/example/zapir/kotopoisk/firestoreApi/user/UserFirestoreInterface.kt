package com.example.zapir.kotopoisk.firestoreApi.user

import com.example.zapir.kotopoisk.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single

interface UserFirestoreInterface {

    fun getUser(userId: String): Single<User>
    fun getCurrentUser(): Single<User>
    fun registerOrUpdateUser(user: User): Single<Unit>
    fun logInWithGoogle(account: GoogleSignInAccount): Single<FirebaseUser>
    fun logOut()

}
