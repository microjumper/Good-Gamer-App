package com.microjumper.goodgamer.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.microjumper.goodgamer.data.api.GameApiService
import com.microjumper.goodgamer.data.models.GameDetail
import com.microjumper.goodgamer.ui.screens.FavoriteScreen
import com.microjumper.goodgamer.ui.screens.GameDetailScreen
import com.microjumper.goodgamer.ui.screens.LoginScreen
import com.microjumper.goodgamer.ui.screens.MainScreen
import com.microjumper.goodgamer.ui.screens.SearchScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    val auth = Firebase.auth
    val isUserLoggedIn = auth.currentUser != null

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) BottomNavItem.Explore.route else "login"
    ) {
        composable("login") {
            LoginScreen() {
                // Navigate to MainScreen after login
                navController.navigate(BottomNavItem.Explore.route) {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        composable(BottomNavItem.Explore.route) {
            MainScreen(
                onGameClick = { gameId ->
                    navController.navigate("gameDetail/$gameId")
                },
                navController = navController
            )
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(onGameClick = { gameId ->
                navController.navigate("gameDetail/$gameId")
            })
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
