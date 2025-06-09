package com.microjumper.goodgamer.data.model

data class GameDetail(
    val id: Long,
    val name: String,
    val rating: Double,
    val backgroundImage: String = "",
    val description: String,
    val genres: List<Genre>,
    val released: String = "",
)

data class Genre(
    val id: Long,
    val name: String,
)