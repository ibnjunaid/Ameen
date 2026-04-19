package com.ameen.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ameen.app.domain.model.OperationMode

@Composable
fun HomeScreen(
    onModeSelected: (OperationMode) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ameen Surveying", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onModeSelected(OperationMode.STANDALONE) },
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Standalone Mode")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onModeSelected(OperationMode.BASE_STATION) },
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Host (Base Station)")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onModeSelected(OperationMode.ROVER) },
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Join (Rover)")
        }
    }
}
