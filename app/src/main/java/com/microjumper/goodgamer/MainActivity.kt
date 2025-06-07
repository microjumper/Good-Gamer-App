package com.microjumper.goodgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.microjumper.goodgamer.mock.GameMock
import com.microjumper.goodgamer.ui.screens.MainScreen
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
                    MainScreen(
                        games = GameMock.topRatedThisYear,
                        onGameClick = { game ->
                            // Handle game click, e.g., navigate to detail screen
                            println("Clicked game: ${game.name}")
                        },
                        onSearchQueryChange = { query ->
                            // Handle search query change
                            println("Search query: $query")
                        }
                    )
                }
            }
        }
    }
}