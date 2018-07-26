package com.example.zapir.kotopoisk.common

class FirestoreProblemsException(msg: String): Exception(msg)
class SerializationException: Exception()
class NonAuthorizedException: Exception()
class NotFoundObject: Exception()
class AlreadyExists: Exception()
