package com.zapir.kotopoisk.data.exceptions

import com.zapir.kotopoisk.R

class NoConnectivityException : Exception()

open class ApiBaseException : Exception() {
    open fun getDefaultRationale(): Int {
        return R.string.default_error_message
    }
}

open class BaseNotFoundException : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.object_not_found
    }
}

class SerializationExceptionApi : ApiBaseException()

class NonAuthorizedExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.auth_error_message
    }
}

//User
class GetUserException : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.user_not_found
    }
}

class UpdateUserExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.update_profile_error
    }
}

//Tickets
class GetTicketException : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.ticket_not_found
    }
}

class GetTicketsListExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.tickets_error
    }
}

class UploadTicketExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.upload_ticket_error
    }
}


class UpdateTicketExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.update_ticket_error
    }
}

class DeleteTicketExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.delete_ticket_error
    }
}



