package com.zapir.kotopoisk.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class FavoriteTicket(val id: String = UUID.randomUUID().toString(),
                     val userId: String = "",
                     val ticketId: String = "") : Parcelable
