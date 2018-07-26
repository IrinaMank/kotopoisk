package com.example.zapir.kotopoisk.model

import java.util.*

data class Photo(val id: String = UUID.randomUUID().toString(),
                 val url: String = "",
                 val userId: String = "",
                 val animalId: String = "")
