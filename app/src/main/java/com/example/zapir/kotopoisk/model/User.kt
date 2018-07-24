package com.example.zapir.kotopoisk.model

data class User(val id: String,
                val nickname:String = "",
                val email: String = "",
                val name: String = "",
                val phone: String = "",
                val petCount: Int = 0,
                val foundPetCount: Int = 0) {
}