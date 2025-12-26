package com.epfl.esl.simongame.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SoloResultScreen(
    score: Int,
    rounds: Int,
    mistakes: Int,
    onPlayAgain: () -> Unit,
    onBackHome: () -> Unit
) {
    val successColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val neutralColor = MaterialTheme.colorScheme.secondary

    val performanceText: String
    val performanceColor: Color
    val emoji: String

    val accuracy: Double = if (rounds > 0) score.toDouble() / rounds else 0.0

    when {
        rounds == 0 || score == 0 -> {
            performanceText = "Warm-up round, try again!"
            performanceColor = neutralColor
            emoji = "🌱"
        }
        mistakes == 0 -> {
            performanceText = "Flawless! Simon is proud of you."
            performanceColor = successColor
            emoji = "🔥"
        }
        accuracy >= 0.8 -> {
            performanceText = "Great job! You’re on fire."
            performanceColor = successColor
            emoji = "⚡️"
        }
        else -> {
            performanceText = "Nice try! You’re getting there."
            performanceColor = neutralColor
            emoji = "👍"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = successColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Game Over",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(performanceColor.copy(alpha = 0.15f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = emoji,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = performanceText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = performanceColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Score: $score",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Rounds played: $rounds")
                Text(text = "Mistakes: $mistakes")

                if (rounds > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Accuracy: ${(accuracy * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onPlayAgain) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Play again"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Play Again")
                    }

                    OutlinedButton(onClick = onBackHome) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Back home"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Home")
                    }
                }
            }
        }
    }
}
