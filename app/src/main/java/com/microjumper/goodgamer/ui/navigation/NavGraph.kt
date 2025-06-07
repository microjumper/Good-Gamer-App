package com.microjumper.goodgamer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.microjumper.goodgamer.mock.GameMock
import com.microjumper.goodgamer.ui.screens.MainScreen
import com.microjumper.goodgamer.ui.screens.SearchScreen
import com.microjumper.goodgamer.ui.screens.FavoriteScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Explore.route
    ) {
        composable(BottomNavItem.Explore.route) {
            MainScreen(games = GameMock.topRatedThisYear, onGameClick = { /*TODO*/ })
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(games = GameMock.topRatedThisYear)
        }
        composable(BottomNavItem.Favorite.route) {
            FavoriteScreen()
        }
    }
}