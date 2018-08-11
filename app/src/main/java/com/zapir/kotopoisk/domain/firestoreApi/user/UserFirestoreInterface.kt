package com.zapir.kotopoisk.domain.firestoreApi.user

import com.zapir.kotopoisk.data.model.User
import com.fernandocejas.arrow.optional.Optional
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Completable
import io.reactivex.Single

interface UserFirestoreInterface {

    fun getUser(userId: String): Single<Optional<User>>
    fun getCurrentUser(): Single<User>
    fun registerUser(user: User): Completable
    fun updateUser(user: User): Completable
    fun isAuthorized(): Single<Boolean>
    fun logInWithGoogle(account: GoogleSignInAccount): Single<User>
    fun logOut()

}
