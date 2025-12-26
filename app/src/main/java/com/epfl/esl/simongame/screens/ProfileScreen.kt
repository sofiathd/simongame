package com.epfl.esl.simongame.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epfl.esl.simongame.R
import com.epfl.esl.simongame.home.HomeUiState

@Composable
private fun PixelPanel(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(2.dp, Color(0xFFD6B15D)) // gold
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.btn_background),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title.uppercase(),
                    fontFamily = PressStart2P,
                    fontSize = 14.sp,
                    color = Color(0xFFE8C46A)
                )
                Spacer(Modifier.height(12.dp))
                content()
            }
        }
    }
}

@Composable
private fun PlayerCard(
    username: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(2.dp, Color(0xFFD6B15D)) // gold
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.btn_background),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.25f))
                        .border(2.dp, Color(0xFF5FE3FF), CircleShape), // cyan ring
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                        fontFamily = PressStart2P,
                        fontSize = 24.sp,
                        color = Color(0xFFE8C46A)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PLAYER",
                        fontFamily = PressStart2P,
                        fontSize = 10.sp,
                        color = Color(0xFFB9B4FF)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = username,
                        fontFamily = PressStart2P,
                        fontSize = 14.sp,
                        color = Color(0xFFE8C46A)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "SIMON SAYS PLAYER",
                        fontFamily = PressStart2P,
                        fontSize = 10.sp,
                        color = Color(0xFF5FE3FF)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileTitle(modifier: Modifier = Modifier) {
    val bungee = FontFamily(androidx.compose.ui.text.font.Font(R.font.bungee_simon))

    @Composable
    fun OutlinedTitle(
        text: String,
        fill: Color,
        outline: Color = Color(0xFF14105A),
        shadowColor: Color = Color(0xFF0A0833),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontFamily = bungee,
                fontSize = 44.sp,
                color = outline,
                style = TextStyle(drawStyle = Stroke(width = 10f))
            )
            Text(
                text = text,
                fontFamily = bungee,
                fontSize = 44.sp,
                color = fill,
                style = TextStyle(
                    shadow = Shadow(
                        color = shadowColor,
                        offset = Offset(6f, 6f),
                        blurRadius = 0f
                    )
                )
            )
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(4.dp))
        OutlinedTitle("PROFILE", fill = Color(0xFF5FE3FF))
    }
}

@Composable
fun ProfileScreen(
    username: String,
    soloStats: HomeUiState,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onFindFriends: () -> Unit,
    onFriendsList: () -> Unit
) {
    val gold = Color(0xFFE8C46A)
    val cyan = Color(0xFF5FE3FF)
    val deep = Color(0xFF0A0833)
    val panel = RoundedCornerShape(18.dp)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "< BACK",
                    fontFamily = PressStart2P,
                    fontSize = 12.sp,
                    color = gold,
                    modifier = Modifier.clickable { onBack() }
                )
            }
            ProfileTitle( modifier = Modifier
                .background(deep.copy(alpha = 0.55f), panel)
                .padding(horizontal = 14.dp, vertical = 10.dp))



            Spacer(Modifier.height(14.dp))

            PlayerCard(username = username)

            Spacer(modifier = Modifier.height(18.dp))

            PixelPanel(title = "Solo stats") {
                Text(
                    text = "BEST SCORE: ${soloStats.bestSoloScore}",
                    fontFamily = PressStart2P,
                    fontSize = 12.sp,
                    color = Color(0xFF5FE3FF)
                )

                Spacer(Modifier.height(10.dp))

                if (soloStats.lastSoloRounds != null && soloStats.lastSoloMistakes != null) {
                    Text(
                        text = "LAST GAME:",
                        fontFamily = PressStart2P,
                        fontSize = 12.sp,
                        color = Color(0xFFE8C46A)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "${soloStats.lastSoloRounds} ROUNDS  |  ${soloStats.lastSoloMistakes} MISTAKES",
                        fontFamily = PressStart2P,
                        fontSize = 11.sp,
                        color = Color(0xFFB9B4FF)
                    )
                } else {
                    Text(
                        text = "NO SOLO GAMES YET",
                        fontFamily = PressStart2P,
                        fontSize = 11.sp,
                        color = Color(0xFFB9B4FF)
                    )
                }

                Spacer(Modifier.height(16.dp))

                FigmaMenuButton(
                    label = "FRIENDS LIST",
                    font = PressStart2P,
                    onClick = onFriendsList,
                    modifier = Modifier.height(62.dp)
                )

                Spacer(Modifier.height(12.dp))

                FigmaMenuButton(
                    label = "FIND FRIENDS",
                    font = PressStart2P,
                    onClick = onFindFriends,
                    modifier = Modifier.height(62.dp)
                )

                Spacer(Modifier.height(12.dp))

                FigmaMenuButton(
                    label = "LOGOUT",
                    font = PressStart2P,
                    onClick = onLogout,
                    modifier = Modifier.height(62.dp)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
