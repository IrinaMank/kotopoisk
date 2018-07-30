package com.example.zapir.kotopoisk.model

import com.example.zapir.kotopoisk.common.Color
import com.example.zapir.kotopoisk.common.FurLength
import com.example.zapir.kotopoisk.common.PetType
import com.example.zapir.kotopoisk.common.Size
import java.util.*

data class BaseTicket(val id: String = UUID.randomUUID().toString(),
                      val lat: Double = -1.0,
                      val lng: Double = -1.0,
                      val date: String = "",
                      val finderId: String = "",
                      var overview: String = "",
                      var type: Int = PetType.CAT.value,
                      var color: Int = Color.BLACK.value,
                      var size: Int = Size.MEDIUM.value,
                      var hasCollar: Boolean = false,
                      var breed: Int? = null,
                      var furLength: Int = FurLength.MEDIUM.value,
                      var isFound: Boolean = false,
                      var isPublished: Boolean = false)