package com.microjumper.goodgamer.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat

import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import com.microjumper.goodgamer.data.models.GameDetail
import com.microjumper.goodgamer.mock.GameDetailMock
import com.microjumper.goodgamer.ui.theme.GoodGamerTheme

@Composable
fun GameDetailScreen(
    gameDetail: GameDetail,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Hero section includes image, back/share/fav buttons, and game title
        GameHeroSection(gameDetail = gameDetail, onBackClick = onBackClick)

        // Main content
        Column(modifier = Modifier.padding(16.dp)) {
            // Genre(s) as chip(s)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()), // Allows horizontal scrolling for many chips
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                gameDetail.genres.forEach { genre ->
                    AssistChip(
                        onClick = { /* TODO: Implement genre click */ },
                        label = { Text(genre.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = HtmlCompat.fromHtml(
                    gameDetail.description,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ).toString(),
                style = MaterialTheme.typography.bodyLarge
            )

            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating star",
                    tint = Color(0xFFFFC107) // Gold color for star
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(gameDetail.rating.toString())
                        }
                        withStyle(
                            style = SpanStyle(
                                // Subtle color for the suffix
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append(" / 5.0")
                        }
                    },
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun GameHeroSection(
    gameDetail: GameDetail,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Game image with content scale
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(gameDetail.background_image)
                .crossfade(true)
                .build(),
            contentDescription = "${gameDetail.name} background image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay to ensure text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // Top controls: back, favorite
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Back Button - Top Left
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            // Right-side - Favorite
            Row {
                IconButton(onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        saveGameToFavorites(userId, gameDetail, context)
                    }

                }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = Color.White
                    )
                }
            }
        }

        // Bottom-aligned title and rating
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = gameDetail.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

fun saveGameToFavorites(userId: String, game: GameDetail, context: Context) {
    val database = FirebaseDatabase.getInstance()
    val gameRef = database.getReference("favorites").child(userId).child(game.id.toString())

    val gameData = mapOf(
        "id" to game.id,
        "name" to game.name,
        "background" to game.background_image
    )

    gameRef.setValue(gameData)
        .addOnSuccessListener {
            Toast.makeText(context, "Game added to favorites", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Failed to add game: ${it.message}", Toast.LENGTH_SHORT).show()
        }
}


@Preview(showBackground = true)
@Composable
fun PreviewGameDetailScreen() {
    GoodGamerTheme {
        GameDetailScreen(gameDetail = GameDetailMock.expedition33, onBackClick = {})
    }
}
