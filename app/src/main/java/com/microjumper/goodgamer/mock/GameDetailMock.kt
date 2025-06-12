package com.microjumper.goodgamer.mock

import com.microjumper.goodgamer.data.models.GameDetail
import com.microjumper.goodgamer.data.models.Genre

object GameDetailMock {
    val expedition33: GameDetail = GameDetail(
        id = 983210,
        name = "Clair Obscur: Expedition 33",
        rating = 4.71f,
        background_image = "https://media.rawg.io/media/games/466/4667f17fdee9ebbcea2049e54f8e2b96.jpg",
        description = "<p>Once a year, the Paintress wakes and paints upon her monolith. Paints her cursed number. And everyone of that age turns to smoke and fades away. Year by year, that number ticks down and more of us are erased. Tomorrow she’ll wake and paint “33.” And tomorrow we depart on our final mission - Destroy the Paintress, so she can never paint death again.</p>\n<p>We are Expedition 33.</p>",
        genres = listOf(
            Genre(id = 5, name = "RPG")
        ),
        released = "2025-04-24"
    )
}