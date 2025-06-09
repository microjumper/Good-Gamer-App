package com.microjumper.goodgamer.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.microjumper.goodgamer.mock.GameDetailMock
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
            MainScreen(gameSummaries = GameSummaryMock.topRatedThisYear, onGameClick = { gameId ->
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
            //val game = GameSummaryMock.topRatedThisYear.find { it.id == gameId }
            val game = GameDetailMock.expedition33

            if (game != null) {
                GameDetailScreen(gameDetail = game, onBackClick = { navController.popBackStack() })
            } else {
                // TODO: Handle case where game is not found (e.g., show error or navigate back)
                Text("Game not found!")
            }
        }
    }
}