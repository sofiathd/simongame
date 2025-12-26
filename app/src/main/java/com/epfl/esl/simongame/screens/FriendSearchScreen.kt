package com.epfl.esl.simongame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import com.epfl.esl.simongame.R
import com.epfl.esl.simongame.data.UserProfile
import com.epfl.esl.simongame.friends.FriendSearchUiState

@Composable
private fun FriendSearchTitle(modifier: Modifier = Modifier) {
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
        OutlinedTitle("FIND", fill = Color(0xFFE8C46A))
        Spacer(Modifier.height(4.dp))
        OutlinedTitle("FRIENDS", fill = Color(0xFF5FE3FF))
    }
}

@Composable
private fun SmallFigmaButton(
    label: String,
    font: FontFamily,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)

    Surface(
        modifier = modifier
            .height(46.dp)
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(2.dp, Color(0xFFD6B15D))
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.btn_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = label,
                fontFamily = font,
                fontSize = 14.sp,
                color = Color(0xFFE8C46A),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun FriendCard(
    user: UserProfile,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(2.dp, Color(0xFFD6B15D))
    ) {
        Box {
            // Same texture as buttons for consistency
            Image(
                painter = painterResource(R.drawable.btn_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
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

                Spacer(Modifier.width(10.dp))

                SmallFigmaButton(
                    label = "ADD",
                    font = PressStart2P,
                    onClick = onAdd,
                    modifier = Modifier.width(88.dp)
                )
            }
        }
    }
}

@Composable
fun FriendSearchScreen(
    ui: FriendSearchUiState,
    onBack: () -> Unit,
    onQueryChange: (String) -> Unit,
    onAddFriend: (String) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val gold = Color(0xFFE8C46A)
        val titleY = maxHeight * 0.08f
        val searchY = maxHeight * 0.30f
        val listY = maxHeight * 0.40f

        // Background like HomeScreen
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        // Title
        FriendSearchTitle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = titleY)
        )

        // Top actions row (Back + optional Logout etc.)
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

        // Search box styled to match theme
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = searchY)
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = ui.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text(
                        text = "Search by username or email",
                        fontFamily = PressStart2P,
                        fontSize = 10.sp,
                        color = Color(0xFFE8C46A)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD6B15D),
                    unfocusedBorderColor = Color(0xFFD6B15D),
                    focusedTextColor = Color(0xFFE8C46A),
                    unfocusedTextColor = Color(0xFFE8C46A),
                    cursorColor = Color(0xFFE8C46A),
                    focusedContainerColor = Color.Black.copy(alpha = 0.25f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.25f),
                    focusedLabelColor = Color(0xFFE8C46A),
                    unfocusedLabelColor = Color(0xFFE8C46A)
                )
            )

            Spacer(Modifier.height(10.dp))

            if (ui.results.isEmpty()) {
                Text(
                    text = if (ui.query.isBlank()) "NO USERS FOUND" else "NO MATCH",
                    fontFamily = PressStart2P,
                    fontSize = 12.sp,
                    color = Color(0xFFB9B4FF)
                )
            }
        }

        // Results list
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = listY)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(ui.results, key = { it.id }) { user ->
                    FriendCard(
                        user = user,
                        onAdd = { onAddFriend(user.id) }
                    )
                }
            }
        }
    }
}
