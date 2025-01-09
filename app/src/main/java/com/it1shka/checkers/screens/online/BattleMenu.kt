package com.it1shka.checkers.screens.online

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BattleMenu(
  nav: NavController,
  nickname: String,
  rating: Int,
  region: String,
  connected: Boolean,
  onReconnect: () -> Unit,
) {
  fun navigateToMenu() {
    nav.popBackStack()
  }

  TextButton(onClick = ::navigateToMenu) {
    Icon(
      Icons.AutoMirrored.Default.KeyboardArrowLeft,
      contentDescription = "Back to menu",
    )
    Text("Menu")
  }

  if (!connected) {
    Column(
      modifier = Modifier
        .fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Icon(
          Icons.Default.Warning,
          contentDescription = "Offline",
          modifier = Modifier
            .scale(2f)
            .padding(vertical = 16.dp),
        )
        Text(
          text = "You are offline",
          style = MaterialTheme.typography.headlineMedium,
        )
        Text(
          text = "Check your internet connection and try again",
          style = MaterialTheme.typography.bodyMedium,
        )
        Button(
          modifier = Modifier.padding(vertical = 12.dp),
          onClick = { onReconnect() }
        ) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Icon(Icons.Default.Refresh, contentDescription = "Reconnect")
            Text(text = "Reconnect")
          }
        }
      }
    }
  }

  if (connected) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    ) {
      Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {

      }
    }
  }
}