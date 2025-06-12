package com.microjumper.goodgamer.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.microjumper.goodgamer.data.models.GameDetail
import com.microjumper.goodgamer.data.models.Genre
import org.json.JSONArray
import com.microjumper.goodgamer.BuildConfig

object GameApiService {
    private val client = OkHttpClient()

    /**
     * Fetch game details from the Firebase Cloud Function REST API
     * @param gameId The ID of the game to fetch
     * @return GameDetail object containing the fetched data
     * @throws Exception if any error occurs (network failure, server error)
     */
    suspend fun fetchGameDetail(gameId: Long): GameDetail {
        // Construct the API endpoint
        val url = "${BuildConfig.GAME_BY_ID_URL}?gameId=$gameId"

        // Build the request
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return withContext(Dispatchers.IO) {
            // Execute the API call
            val response = client.newCall(request).execute()

            // Handle errors
            if (!response.isSuccessful) {
                throw Exception("Failed to fetch game details: ${response.code} ${response.message}")
            }

            // Parse response body
            val responseBody = response.body?.string() ?: throw Exception("Empty response from server")

            // Deserialize JSON into GameDetail object
            val jsonObject = JSONObject(responseBody)
            GameDetail(
                id = jsonObject.getLong("id"),
                name = jsonObject.getString("name"),
                description = jsonObject.getString("description"),
                rating = jsonObject.getDouble("rating").toFloat(),
                background_image = jsonObject.getString("background_image"),
                genres =  parseGenres(jsonObject.getJSONArray("genres")),
                released = jsonObject.getString("released")
            )
        }
    }
}

fun parseGenres(jsonArray: JSONArray): List<Genre> {
    val genres = mutableListOf<Genre>()

    for (i in 0 until jsonArray.length()) {
        val genreJsonObject = jsonArray.getJSONObject(i)
        genres.add(
            Genre(
                id = genreJsonObject.getLong("id"),
                name = genreJsonObject.getString("name")
            )
        )
    }

    return genres
}
