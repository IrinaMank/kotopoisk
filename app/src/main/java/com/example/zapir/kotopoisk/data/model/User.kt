package com.example.zapir.kotopoisk.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(var id: String = UUID.randomUUID().toString(),
                var nickname: String = "",
                var email: String = "",
                var name: String = "",
                var phone: String = "",
                var petCount: Int = 0,
                var foundPetCount: Int = 0) : Parcelable