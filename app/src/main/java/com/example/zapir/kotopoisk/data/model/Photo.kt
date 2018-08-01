package com.example.zapir.kotopoisk.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Photo(val id: String = UUID.randomUUID().toString(),
                 val url: String = "",
                 val userId: String = "",
                 val ticketId: String = ""): Parcelable
