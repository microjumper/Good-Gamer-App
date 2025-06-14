package com.microjumper.goodgamer.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import okhttp3.OkHttpClient
import okhttp3.Request

import org.json.JSONObject
import org.json.JSONArray

import com.microjumper.goodgamer.BuildConfig
import com.microjumper.goodgamer.data.models.GameDetail
import com.microjumper.goodgamer.data.models.Genre
import com.microjumper.goodgamer.data.models.Game

object GameApiService {
    private val client = OkHttpClient()

    /**
     * Generic function to execute an HTTP GET request.
     * @param url The URL of the API endpoint.
     * @return The response body as a String.
     * @throws Exception if the response is unsuccessful or the body is empty.
     */
    private suspend fun executeGetRequest(url: String): String {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return withContext(Dispatchers.IO) {
            val response = client.newCall(request).execute()

            // Handle unsuccessful responses
            if (!response.isSuccessful) {
                throw Exception("Request failed: ${response.code} ${response.message}")
            }

            // Extract and return the response body
            response.body?.string() ?: throw Exception("Empty response from server")
        }
    }

    /**
     * Fetch game details from the API endpoint.
     * @param gameId The ID of the game to fetch.
     * @return GameDetail object containing the fetched data.
     * @throws Exception if an error occurs.
     */
    suspend fun fetchGameDetail(gameId: Long): GameDetail {
        val url = "${BuildConfig.GAME_BY_ID_URL}?gameId=$gameId"

        // Execute the request and parse the response
        val responseBody = executeGetRequest(url)
        val jsonObject = JSONObject(responseBody)

        return GameDetail(
            id = jsonObject.getLong("id"),
            name = jsonObject.getString("name"),
            description = jsonObject.getString("description"),
            rating = jsonObject.getDouble("rating").toFloat(),
            background_image = jsonObject.getString("background_image"),
            genres = parseGenres(jsonObject.getJSONArray("genres")),
            released = jsonObject.getString("released")
        )
    }

    /**
     * Search games by name using the API endpoint.
     * @param gameName The name of the game to search for.
     * @return A list of Game objects matching the search criteria.
     * @throws Exception if an error occurs.
     */
    suspend fun searchGamesByName(gameName: String): List<Game> {
        val url = "${BuildConfig.SEARCH_GAMES_URL}?name=$gameName"

        // Execute the request and parse the response
        val responseBody = executeGetRequest(url)
        val jsonArray = JSONArray(responseBody)

        return parseGames(jsonArray)
    }

    /**
     * Parse a JSON array into a list of Genre objects.
     * @param jsonArray The JSON array to parse.
     * @return A list of Genre objects.
     */
    private fun parseGenres(jsonArray: JSONArray): List<Genre> {
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

    /**
     * Parse a JSON array into a list of Game objects.
     * @param jsonArray The JSON array to parse.
     * @return A list of Game objects.
     */
    private fun parseGames(jsonArray: JSONArray): List<Game> {
        val games = mutableListOf<Game>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            games.add(
                Game(
                    id = jsonObject.getLong("id"),
                    name = jsonObject.getString("name"),
                    background_image = jsonObject.getString("background_image")
                )
            )
        }

        return games
    }
}