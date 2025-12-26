package com.epfl.esl.simongame.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.epfl.esl.simongame.R
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp


enum class SoloPhase { WaitingWatch, WatchDemo, YourTurn, Checking, GameOver }

data class SoloMotionUiState(
    val phase: SoloPhase,
    val isWatchConnected: Boolean,
    val round: Int,
    val score: Int,
    val mistakes: Int,
    val timeLeftSec: Int,
    val instruction: String,
)

@Composable
fun SoloGameScreen(
    ui: SoloMotionUiState,
    onBack: () -> Unit,
    onPause: () -> Unit,
    onRestart: () -> Unit,
    onCalibrate: () -> Unit,
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

            Row(
                modifier = Modifier.fillMaxWidth().padding(top=10.dp),
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
                    text = "SOLO",
                    fontFamily = PressStart2P,
                    fontSize = 12.sp,
                    color = cyan
                )
            }

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .border(2.dp, if (ui.isWatchConnected) cyan else gold, panel)
                    .background(deep.copy(alpha = 0.55f), panel)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (ui.isWatchConnected) "WATCH CONNECTED" else "CONNECT WATCH...",
                    fontFamily = PressStart2P,
                    fontSize = 11.sp,
                    color = if (ui.isWatchConnected) cyan else gold
                )
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, gold, panel)
                    .background(Color(0xFF15104A).copy(alpha = 0.55f), panel)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Stat("ROUND", ui.round.toString(), gold, PressStart2P)
                Stat("SCORE", ui.score.toString(), gold, PressStart2P)
                Stat("ERR", ui.mistakes.toString(), gold, PressStart2P)
            }

            Spacer(Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .border(2.dp, cyan, panel)
                    .background(deep.copy(alpha = 0.55f), panel),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (ui.phase) {
                            SoloPhase.WaitingWatch -> "WAITING..."
                            SoloPhase.WatchDemo -> "WATCH DEMO"
                            SoloPhase.YourTurn -> "YOUR TURN"
                            SoloPhase.Checking -> "CHECKING..."
                            SoloPhase.GameOver -> "GAME OVER"
                        },
                        fontFamily = PressStart2P,
                        fontSize = 12.sp,
                        color = cyan
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = ui.instruction,
                        fontFamily = PressStart2P,
                        fontSize = 18.sp,
                        color = gold
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "TIME ${ui.timeLeftSec}s",
                        fontFamily = PressStart2P,
                        fontSize = 12.sp,
                        color = gold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CountdownRing(
                    timeLeftSec = ui.timeLeftSec,
                    totalSec = 5,
                    ringSize = 160.dp
                )
            }


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    FigmaMenuButton("PAUSE", PressStart2P, onClick = onPause, modifier = Modifier.weight(1f).height(64.dp))
                    FigmaMenuButton("RESTART", PressStart2P, onClick = onRestart, modifier = Modifier.weight(1f).height(64.dp))
                }
                FigmaMenuButton("CALIBRATE", PressStart2P, onClick = onCalibrate, modifier = Modifier.height(64.dp))
            }
        }
    }
}


@Composable
fun CountdownRing(
    timeLeftSec: Int,
    totalSec: Int,
    modifier: Modifier = Modifier,
    ringSize: Dp = 150.dp,
    strokeWidth: Dp = 12.dp,
    trackColor: Color = Color(0xFF0A0833).copy(alpha = 0.5f),
    progressColor: Color = Color(0xFF5FE3FF), // cyan
    textColor: Color = Color(0xFFE8C46A),     // gold
) {
    val rawProgress = (timeLeftSec.coerceIn(0, totalSec)).toFloat() / totalSec.toFloat()
    val progress = animateFloatAsState(targetValue = rawProgress, label = "ringProgress").value

    Box(modifier = modifier.size(ringSize), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val diameter = size.minDimension
            val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
            val arcSize = Size(diameter, diameter)

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = stroke
            )
        }

        Text(
            text = timeLeftSec.toString(),
            fontFamily = PressStart2P,
            fontSize = 28.sp,
            color = textColor
        )
    }
}

@Composable
private fun Stat(label: String, value: String, color: Color, font: FontFamily) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontFamily = font, fontSize = 9.sp, color = color)
        Spacer(Modifier.height(4.dp))
        Text(value, fontFamily = font, fontSize = 12.sp, color = color)
    }
}
