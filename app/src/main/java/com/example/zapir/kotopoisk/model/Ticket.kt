package com.example.zapir.kotopoisk.model

data class Ticket(val id: String = "",
                  val lat: Double = 0.0,
                  val lng: Double = 0.0,
                  val date: String = "",
                  val finderId: String = "",
                  val animalId: String = "",
                  val overview: String = "",
                  val isPublished: Boolean = false
)