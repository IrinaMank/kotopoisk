package com.example.zapir.kotopoisk.firestoreApi.user

import com.example.zapir.kotopoisk.model.User

interface UserFirestoreInterface {

    fun getUser(userId: String, completion: (User?) -> Unit)
    fun getMe(completion: (User) -> Unit = {})
    fun updateMe(user: User, completion: () -> Unit)
    fun logIn(completion: () -> Unit)
    fun logOut(completion: () -> Unit)

}
