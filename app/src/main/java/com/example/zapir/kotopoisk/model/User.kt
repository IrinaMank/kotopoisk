package com.example.zapir.kotopoisk.model

import java.util.*

data class User(val id: String = UUID.randomUUID().toString(),
                var nickname: String = "",
                var email: String = "",
                var name: String = "",
                var phone: String = "",
                var petCount: Int = 0,
                var foundPetCount: Int = 0) {
}
