package com.example.zapir.kotopoisk.model

data class Animal(val id: String = "",
                  val type: Int = 0,
                  val color: Int = 0,
                  val size: Int = 0,
                  val hasCollar: Boolean = false,
                  val breed: Int? = 0,
                  val furLength: Int = 0,
                  val isFound: Boolean = false)