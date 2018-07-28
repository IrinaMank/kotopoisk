package com.example.zapir.kotopoisk.common

import android.content.Context
import com.example.zapir.kotopoisk.R

object TypesConverter {
    
    fun getColorString(type: Int, context: Context) =
        when (type) {
            0 -> context.getString(R.string.white)
            1 -> context.getString(R.string.black)
            2 -> context.getString(R.string.red)
            3 -> "Gray"
            else -> "Unknown"

    }

    fun getTypeString(type: Int, context: Context) =
        when (type) {
            0 -> context.getString(R.string.cat)
            1 -> context.getString(R.string.dog)
            else -> context.getString(R.string.unknown)

    }

    fun getSizeString(type: Int, context: Context) =
        when (type) {
            0 -> context.getString(R.string.short_word)
            1 -> context.getString(R.string.medium)
            2 -> context.getString(R.string.big)
            else -> context.getString(R.string.unknown)

    }

    fun getFurLengthString(type: Int, context: Context): String =
        when (type) {
            0 -> context.getString(R.string.short_word)
            1 -> context.getString(R.string.medium)
            2 -> context.getString(R.string.long_word)
            else -> context.getString(R.string.unknown)
        }

}