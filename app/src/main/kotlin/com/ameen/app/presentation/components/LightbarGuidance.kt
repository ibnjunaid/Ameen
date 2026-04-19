package com.ameen.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun LightbarGuidance(
    crossTrackError: Double, // in meters
    maxDeviation: Double = 2.0 // meters for full scale
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Cross Track Error: ${String.format("%.2f", crossTrackError)}m")
        Spacer(modifier = Modifier.height(8.dp))
        Canvas(modifier = Modifier.fillMaxWidth().height(40.dp)) {
            val width = size.width
            val height = size.height
            val center = width / 2

            // Draw scale
            drawLine(Color.Gray, Offset(0f, height/2), Offset(width, height/2), strokeWidth = 2f)
            drawLine(Color.Gray, Offset(center, 0f), Offset(center, height), strokeWidth = 4f)

            // Draw Indicator
            val fraction = (crossTrackError / maxDeviation).coerceIn(-1.0, 1.0)
            val indicatorPos = center + (fraction * center).toFloat()
            val color = if (crossTrackError.absoluteValue < 0.20) Color.Green else Color.Red

            drawCircle(
                color = color,
                radius = 15f,
                center = Offset(indicatorPos, height / 2)
            )
        }
    }
}
