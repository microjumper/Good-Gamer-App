package com.microjumper.goodgamer.data.model

data class Game(
    val id: Long = 0,
    val name: String = "",
    val released: String = "",
    val backgroundImage: String = "",
    val rating: Float = 0.0f
)