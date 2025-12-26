package com.epfl.esl.simongame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epfl.esl.simongame.R

@Composable
fun RegisterScreen(
    onRegister: (username: String, email: String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val titleY = maxHeight * 0.10f
        val formY = maxHeight * 0.42f

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

        HomeTitle(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = titleY)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = formY)
                .padding(horizontal = 28.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "REGISTER",
                fontFamily = PressStart2P,
                fontSize = 16.sp,
                color = Color(0xFFE8C46A)
            )

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text(
                        "Username",
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
                ),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text(
                        "Email (optional)",
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
                ),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(Modifier.height(18.dp))

            FigmaMenuButton(
                label = "CONTINUE",
                font = PressStart2P,
                onClick = { onRegister(username.trim(), email.trim()) },
                modifier = Modifier.height(72.dp)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = if (username.trim().isEmpty()) "ENTER A USERNAME TO CONTINUE" else "",
                fontFamily = PressStart2P,
                fontSize = 10.sp,
                color = Color(0xFFB9B4FF)
            )
        }
    }
}
