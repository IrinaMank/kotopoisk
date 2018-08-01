package com.example.zapir.kotopoisk.data.model

import com.example.zapir.kotopoisk.domain.common.Color
import com.example.zapir.kotopoisk.domain.common.FurLength
import com.example.zapir.kotopoisk.domain.common.PetType
import com.example.zapir.kotopoisk.domain.common.Size
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