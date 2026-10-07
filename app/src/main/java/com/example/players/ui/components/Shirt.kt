package com.example.players.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Shirt(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    color: Color = Color.Black,
    number: Int? = null
) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val pts = listOf(
                0.30f to 0.05f, 0.40f to 0.05f, 0.50f to 0.18f, 0.60f to 0.05f,
                0.70f to 0.05f, 1.00f to 0.25f, 0.85f to 0.45f, 0.75f to 0.38f,
                0.75f to 0.95f, 0.25f to 0.95f, 0.25f to 0.38f, 0.15f to 0.45f,
                0.00f to 0.25f
            )
            val path = Path().apply {
                moveTo(pts[0].first * w, pts[0].second * h)
                pts.drop(1).forEach { lineTo(it.first * w, it.second * h) }
                close()
            }
            drawPath(path, color)
        }
        if (number != null) {
            Text(
                text = number.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.4f).sp
            )
        }
    }
}
