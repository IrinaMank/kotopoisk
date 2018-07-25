package com.example.zapir.kotopoisk.common

class NetworkProblemsException(msg: String): Exception(msg)
class SerializationException: Exception()
class NotFoundObject: Exception()
class AlreadyExists: Exception()
