package com.locker.catapp.model

class NoSuchPokemonException(override val message: String? = "Could not find that Pokemon!") :
    Exception()