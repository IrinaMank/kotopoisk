package com.example.zapir.kotopoisk.model

import com.example.zapir.kotopoisk.common.Color
import com.example.zapir.kotopoisk.common.FurLength
import com.example.zapir.kotopoisk.common.PetType
import com.example.zapir.kotopoisk.common.Size
import java.util.*

data class Ticket(val id: String = UUID.randomUUID().toString(),
                  val lat: Double = -1.0,
                  val lng: Double = -1.0,
                  val date: String = "",
                  val finderId: String = "",
                  val animalId: String = "",
                  var overview: String = "",
                  val type: Int = PetType.CAT.value,
                  val color: Int = Color.BLACK.value,
                  val size: Int = Size.MEDIUM.value,
                  val hasCollar: Boolean = false,
                  val breed: Int? = null,
                  val furLength: Int = FurLength.MEDIUM.value,
                  val isFound: Boolean = false,
                  var isPublished: Boolean = false
)