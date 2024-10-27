package com.it1shka.checkers.screens.battle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth

data class BattleMode (
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
)

private val battleModes = listOf(
  BattleMode(
    title = "Play Online",
    subtitle = "Online mode",
    icon = Icons.Filled.Search,
  ),
  BattleMode(
    title = "Play with Computer",
    subtitle = "Offline mode",
    icon = Icons.Filled.Home,
  ),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable fun Battle() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(top = 40.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = "Checkers Online",
      style = MaterialTheme.typography.headlineLarge,
    )
    Column (
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      battleModes.forEach {
        BattleModeCard(it)
      }
    }
  }
}