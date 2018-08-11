package com.zapir.kotopoisk.domain.common

import android.content.Context
import com.zapir.kotopoisk.R

object TypesConverter {

    fun getStringFromColor(color: Int, context: Context) =
            context.resources.getStringArray(R.array.color_array)[color]

    fun getColorFromString(color: String, context: Context) =
            context.resources.getStringArray(R.array.color_array).asList().indexOf(color)

    fun getStringFromType(type: Int, context: Context) =
            when (type) {
                0 -> context.getString(R.string.cat)
                1 -> context.getString(R.string.dog)
                else -> context.getString(R.string.unknown)
            }

    fun getTypeFromString(type: String, context: Context) =
            when (type) {
                context.getString(R.string.cat) -> 0
                context.getString(R.string.dog) -> 1
                else -> -1
            }

    fun getStringFromSize(size: Int, type: Int, context: Context) =
            when (type) {
                0 -> context.resources.getStringArray(R.array.cat_size)[size]
                1 -> context.resources.getStringArray(R.array.dog_size)[size]
                else -> context.getString(R.string.unknown)
            }

    fun getSizeFromString(size: String, type: Int, context: Context) =
            when (type) {
                0 -> context.resources.getStringArray(R.array.cat_size).asList().indexOf(size)
                1 -> context.resources.getStringArray(R.array.dog_size).asList().indexOf(size)
                else -> -1
            }

    fun getStringFromFurLength(furLength: Int, context: Context) =
            context.resources.getStringArray(R.array.fur_length_array)[furLength]

    fun getFurLengthFromString(furLength: String, context: Context) =
            context.resources.getStringArray(R.array.fur_length_array).asList().indexOf(furLength)

    fun getStringFromBreed(breed: Int, type: Int, context: Context) =
            when (type) {
                0 -> context.resources.getStringArray(R.array.cat_breed_array)[breed]
                1 -> context.resources.getStringArray(R.array.dog_breed_array)[breed]
                else -> context.getString(R.string.unknown)
            }

    fun getBreedFromString(breed: String, type: Int, context: Context) =
            when (type) {
                0 -> context.resources.getStringArray(R.array.cat_breed_array).asList().indexOf(breed)
                1 -> context.resources.getStringArray(R.array.dog_breed_array).asList().indexOf(breed)
                else -> -1
            }

}