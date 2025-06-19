package com.microjumper.goodgamer.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.microjumper.goodgamer.data.models.Game

@Composable
fun FavoriteScreen() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val favorites = remember { mutableStateListOf<Game>() }

    LaunchedEffect(userId) {
        if (userId != null) {
            fetchFavorites(userId) { games ->
                favorites.clear()
                favorites.addAll(games)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title section
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.headlineMedium, // Adjust style as per your theme
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            fontSize = 24.sp
        )

        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No favorites found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites) { game ->
                    Text(
                        text = game.name,
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                    )
                }
            }
        }
    }
}

/**
 * Fetches the list of favorite games from Firebase Realtime Database and passes them to a callback function.
 */
fun fetchFavorites(userId: String, onFavoritesLoaded: (List<Game>) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val favoritesRef = database.getReference("favorites").child(userId)

    favoritesRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val games = snapshot.children.mapNotNull { child ->
                child.getValue(Game::class.java)
            }
            onFavoritesLoaded(games)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("FavoriteScreen", "Failed to fetch favorites: ${error.message}")
        }
    })
}