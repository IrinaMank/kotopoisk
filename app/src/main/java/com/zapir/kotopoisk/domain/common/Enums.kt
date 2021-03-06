package com.zapir.kotopoisk.domain.common

enum class Color(val value: Int) {
    WHITE(0),
    BLACK(1),
    RED(2),
    GREY(3),
    ORANGE(4),
    PINK(5)

}

enum class PetType(val value: Int) {
    CAT(0),
    DOG(1),
    DEFAULT(2)

}

enum class Size(val value: Int) {
    SMALL(0),
    MEDIUM(1),
    BIG(2),

}

enum class FurLength(val value: Int) {
    SHORT(0),
    MEDIUM(1),
    LONG(2),

}

//enum class TicketType(val value: Boolean) {
//    SAVED(true),
//    FAVOURITE(false),
//
//}

enum class SelectedPage(val value: Int) {
    SEARCH(0),
    MAP(1),
    PROFILE(2)

}