package com.example.zapir.kotopoisk.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Photo(val id: String = UUID.randomUUID().toString(),
                 var url: String = "",
                 var userId: String = "",
                 var ticketId: String = "") : Parcelable
