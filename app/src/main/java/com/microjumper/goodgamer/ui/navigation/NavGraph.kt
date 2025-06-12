package com.microjumper.goodgamer.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.microjumper.goodgamer.data.api.GameApiService
import com.microjumper.goodgamer.data.models.GameDetail
import com.microjumper.goodgamer.mock.GameSummaryMock
import com.microjumper.goodgamer.ui.screens.MainScreen
import com.microjumper.goodgamer.ui.screens.SearchScreen
import com.microjumper.goodgamer.ui.screens.FavoriteScreen
import com.microjumper.goodgamer.ui.screens.GameDetailScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Explore.route
    ) {
        composable(BottomNavItem.Explore.route) {
            MainScreen(onGameClick = { gameId ->
                navController.navigate("gameDetail/$gameId")
            })
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(gameSummaries = GameSummaryMock.topRatedThisYear)
        }
        composable(BottomNavItem.Favorite.route) {
            FavoriteScreen()
        }

        composable(
            route = "gameDetail/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.LongType })
        ) { backStackEntry ->

            val gameId = backStackEntry.arguments?.getLong("gameId")

            var gameDetail by remember { mutableStateOf<GameDetail?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(gameId) {
                gameId?.let {
                    try {
                        gameDetail = GameApiService.fetchGameDetail(it)
                    } catch (e: Exception) {
                        errorMessage = e.localizedMessage ?: "Failed to load game details"
                        Log.e("GameDetailScreen", errorMessage, e)
                    }
                } ?: run {
                    errorMessage = "Game ID is null"
                    Log.e("GameDetailScreen", errorMessage!!)
                }
            }

            when {
                gameDetail != null -> {
                    GameDetailScreen(
                        gameDetail = gameDetail!!,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                errorMessage != null -> {
                    Text(
                        text = "Error: $errorMessage",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    // Loading state while the data is being fetched
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
