package com.example.zapir.kotopoisk.data.exceptions

class UnknownPetId : Exception()
class UnknownPetType : Exception()
class PetNotFound(msg: String) : Exception(msg)