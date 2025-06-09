package com.microjumper.goodgamer.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.microjumper.goodgamer.mock.GameMock
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
            MainScreen(games = GameMock.topRatedThisYear, onGameClick = { gameId ->
                navController.navigate("gameDetail/$gameId")
            })
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(games = GameMock.topRatedThisYear)
        }
        composable(BottomNavItem.Favorite.route) {
            FavoriteScreen()
        }

        composable(
            route = "gameDetail/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.LongType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getLong("gameId")
            val game = GameMock.topRatedThisYear.find { it.id == gameId }

            if (game != null) {
                GameDetailScreen(game = game, onBackClick = { navController.popBackStack() })
            } else {
                // TODO: Handle case where game is not found (e.g., show error or navigate back)
                Text("Game not found!")
            }
        }
    }
}