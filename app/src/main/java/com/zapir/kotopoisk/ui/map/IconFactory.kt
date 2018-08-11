package com.zapir.kotopoisk.ui.map

import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.exceptions.UnknownPetType
import com.zapir.kotopoisk.domain.common.PetType
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

class IconFactory {
    companion object {

        fun getBitmapDescriptionPetIcon(petType: Int): BitmapDescriptor {
            return BitmapDescriptorFactory.fromResource(
                    when (petType) {
                        PetType.DEFAULT.value -> R.drawable.ic_marker
                        PetType.CAT.value -> R.drawable.ic_cat
                        PetType.DOG.value -> R.drawable.ic_dog
                        else -> throw UnknownPetType()
                    }
            )
        }

    }
}