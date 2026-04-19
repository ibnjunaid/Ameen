package com.ameen.app.presentation.survey

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ameen.app.domain.model.LocationData
import com.ameen.app.presentation.components.LightbarGuidance

@Composable
fun SurveyScreen(
    currentLocation: LocationData?,
    crossTrackError: Double,
    distanceToFinish: Double,
    isL5: Boolean
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Satellite Status", style = MaterialTheme.typography.titleMedium)
                    Text("GNSS Band: ${if (isL5) "L1 + L5 (High Precision)" else "L1 Only"}")
                    currentLocation?.let {
                        Text("Accuracy: ${String.format("%.2f", it.accuracy)}m")
                    }
                }
            }

            LightbarGuidance(crossTrackError = crossTrackError)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoTile(label = "Dist to Finish", value = "${String.format("%.1f", distanceToFinish)}m")
                InfoTile(label = "Speed", value = "${String.format("%.1f", currentLocation?.speed ?: 0f)} m/s")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Placeholder for Google Maps
            Surface(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Map View")
                }
            }
        }
    }
}

@Composable
fun InfoTile(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.headlineSmall)
    }
}
