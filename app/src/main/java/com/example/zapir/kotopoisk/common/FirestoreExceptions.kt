package com.example.zapir.kotopoisk.common

import com.example.zapir.kotopoisk.R

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
class getUserException : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.user_not_found
    }
}

class updateUserExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.update_profile_error
    }
}

//Tickets
class getTicketException : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.ticket_not_found
    }
}

class getTicketsListExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.tickets_error
    }
}

class uploadTicketExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.upload_ticket_error
    }
}


class updateTicketExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.update_ticket_error
    }
}

class deleteTicketExceptionApi : ApiBaseException() {
    override fun getDefaultRationale(): Int {
        return R.string.delete_ticket
    }
}



