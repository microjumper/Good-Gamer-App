package com.microjumper.goodgamer.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import com.microjumper.goodgamer.R
import com.microjumper.goodgamer.data.models.Game
import com.microjumper.goodgamer.mock.GameMock
import com.microjumper.goodgamer.ui.components.GameCard

private const val TAG = "MainScreen"

@Composable
fun MainScreen(onGameClick: (Long) -> Unit) {
    var games by remember { mutableStateOf<List<Game>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val database = remember { Firebase.database }
    val topRatedRef = remember { database.getReference("topRatedThisYear") }

    // Prevent stale reference to onGameClick
    val currentOnGameClick by rememberUpdatedState(onGameClick)

    // Fetch data once and set up listener cleanup
    DisposableEffect(Unit) {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                games = snapshot.getValue<List<Game>>() as List<Game>
                isLoading = false
                Log.d(TAG, "Loaded ${games.size} games")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Firebase error: ${error.message}", error.toException())
                games = emptyList()
                isLoading = false
            }
        }

        topRatedRef.addValueEventListener(listener)
        Log.d(TAG, "Firebase listener attached")

        onDispose {
            topRatedRef.removeEventListener(listener)
            Log.d(TAG, "Firebase listener detached")
        }
    }

    MainScreenContent(
        gameSummaries = games,
        isLoading = isLoading,
        onGameClick = currentOnGameClick
    )
}

@Composable
private fun MainScreenContent(
    gameSummaries: List<Game>,
    isLoading: Boolean,
    onGameClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.top_rated_this_year),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        when {
            isLoading -> LoadingView()
            gameSummaries.isEmpty() -> EmptyView()
            else -> GameGrid(games = gameSummaries, onGameClick = onGameClick)
        }
    }
}

@Composable
private fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Loading top games...", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun EmptyView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No top games found.", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun GameGrid(
    games: List<Game>,
    onGameClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(games, key = { it.id }) { game ->
            GameCard(game = game, onClick = { onGameClick(game.id) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreenContent(
        gameSummaries = GameMock.topRatedThisYear,
        isLoading = false,
        onGameClick = {}
    )
}
