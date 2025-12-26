package com.epfl.esl.simongame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.epfl.esl.simongame.R
import com.epfl.esl.simongame.data.UserProfile
import com.epfl.esl.simongame.friends.FriendsListUiState

@Composable
private fun FriendListTitle(modifier: Modifier = Modifier) {
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
        OutlinedTitle("FRIENDS", fill = Color(0xFFE8C46A))
        Spacer(Modifier.height(4.dp))
        OutlinedTitle("LIST", fill = Color(0xFF5FE3FF))
    }
}

@Composable
private fun FriendListCard(
    user: UserProfile,
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
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = user.username,
                    fontFamily = PressStart2P,
                    fontSize = 14.sp,
                    color = Color(0xFFE8C46A)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = user.email,
                    fontFamily = PressStart2P,
                    fontSize = 10.sp,
                    color = Color(0xFFB9B4FF)
                )
            }
        }
    }
}

@Composable
fun FriendsListScreen(
    ui: FriendsListUiState,
    onBack: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val gold = Color(0xFFE8C46A)
        val titleY = maxHeight * 0.08f
        val listY = maxHeight * 0.30f

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

        FriendListTitle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = titleY)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top=30.dp).padding(horizontal=24.dp),
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

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = listY)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (ui.friends.isEmpty()) {
                Text(
                    text = "NO FRIENDS YET",
                    fontFamily = PressStart2P,
                    fontSize = 12.sp,
                    color = Color(0xFFB9B4FF)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(ui.friends, key = { it.id }) { user ->
                        FriendListCard(user = user)
                    }
                }
            }
        }
    }
}
