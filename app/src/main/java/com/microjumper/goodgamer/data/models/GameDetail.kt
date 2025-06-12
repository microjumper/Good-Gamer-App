package com.microjumper.goodgamer.data.models

data class GameDetail (
    val id: Long = 0,
    val name: String = "",
    val background_image: String = "",
    val rating: Float,
    val description: String,
    val genres: List<Genre>,
    val released: String = ""
)

data class Genre(
    val id: Long,
    val name: String,
)