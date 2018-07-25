package com.example.zapir.kotopoisk.model

import java.util.*

data class Ticket(val id: String = UUID.randomUUID().toString(),
                  val lat: Double = 0.0,
                  val lng: Double = 0.0,
                  val date: String = "",
                  val finderId: String = "",
                  val animalId: String = "",
                  var overview: String = "",
                  var isPublished: Boolean = false
)