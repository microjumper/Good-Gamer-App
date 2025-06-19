package com.microjumper.goodgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.microjumper.goodgamer.ui.components.BottomNavigationBar
import com.microjumper.goodgamer.ui.navigation.SetupNavGraph
import com.microjumper.goodgamer.ui.theme.GoodGamerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoodGamerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry = navController.currentBackStackEntryAsState().value
                    val currentRoute = navBackStackEntry?.destination?.route

                    // Define routes where BottomNavigationBar should NOT be shown
                    val showBottomBar = currentRoute != "login"

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                BottomNavigationBar(navController = navController)
                            }
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            SetupNavGraph(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
