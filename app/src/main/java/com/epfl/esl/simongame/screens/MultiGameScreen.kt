package com.epfl.esl.simongame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epfl.esl.simongame.R


@Composable
fun MultiGameScreen(
    //ui: SoloMotionUiState,
    onBack: () -> Unit,
    //onPause: () -> Unit,
    //onRestart: () -> Unit,
    //onCalibrate: () -> Unit,
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
            Modifier
                .matchParentSize()
                .background(Color.Black.copy(alpha = 0.35f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "< BACK",
                    fontFamily = PressStart2P,
                    fontSize = 12.sp,
                    color = gold,
                    modifier = Modifier.clickable { onBack() }
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "MULTIPLAYER",
                    fontFamily = PressStart2P,
                    fontSize = 12.sp,
                    color = cyan
                )
            }
        }
    }
}