package com.microjumper.goodgamer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Explore : BottomNavItem("explore", "Explore", Icons.Default.Home)
    object Search : BottomNavItem("search", "Search", Icons.Default.Search)
    object Favorite : BottomNavItem("favorite", "Favorites", Icons.Default.Favorite)

    companion object {
        val entries = listOf<BottomNavItem>(Explore, Search, Favorite)
    }
}