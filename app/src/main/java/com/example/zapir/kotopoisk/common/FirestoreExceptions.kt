package com.example.zapir.kotopoisk.common

class NetworkProblemsException(msg: String): Exception(msg)

class NoSuchElementException: Exception()
class ElementAlreadyExistsException: Exception()
class SerializationException: Exception()