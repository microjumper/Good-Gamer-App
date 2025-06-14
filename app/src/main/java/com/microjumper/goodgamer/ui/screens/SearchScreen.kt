package com.microjumper.goodgamer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.microjumper.goodgamer.R
import com.microjumper.goodgamer.data.api.GameApiService
import com.microjumper.goodgamer.data.models.Game
import com.microjumper.goodgamer.ui.components.GameCard
import com.microjumper.goodgamer.ui.theme.GoodGamerTheme

import kotlinx.coroutines.launch

@Composable
fun SearchScreen(onGameClick: (Long) -> Unit) {
    // State for the search query input
    var query by remember { mutableStateOf("") }

    // State for the list of games fetched from the API
    var games by remember { mutableStateOf<List<Game>>(emptyList()) }

    // State for loading and error indication
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Coroutine scope for performing asynchronous operations
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Search text field
                OutlinedTextField(
                    value = query,
                    onValueChange = { newQuery ->
                        query = newQuery
                        // Trigger a new search only when the query changes
                        coroutineScope.launch {
                            if (query.isNotEmpty()) {
                                try {
                                    isLoading = true
                                    errorMessage = null
                                    // Fetch matching games from the API
                                    games = GameApiService.searchGamesByName(query)
                                } catch (e: Exception) {
                                    // Handle any errors during the API call
                                    errorMessage = "Failed to load games."
                                } finally {
                                    isLoading = false
                                }
                            } else {
                                // Clear the games list if the query becomes empty
                                games = emptyList()
                            }
                        }
                    },
                    label = { Text(text = stringResource(id = R.string.search_hint)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = MaterialTheme.shapes.medium, // Rounded corners
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            Text(
                                text = "Clear",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .clickable { query = "" })
                        }
                    }
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Top
            ) {
                // Display a loading indicator if the data is loading
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                // Display error message, if any
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                // Display the grid of games
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(games) { game ->
                        GameCard(
                            game = game,
                            onClick = { onGameClick(game.id) },
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    GoodGamerTheme {
        SearchScreen(
            onGameClick = {}
        )
    }
}