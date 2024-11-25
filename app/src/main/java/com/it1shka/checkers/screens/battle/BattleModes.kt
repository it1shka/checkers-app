package com.it1shka.checkers.screens.battle

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class BattleModeType {
  ONLINE,
  OFFLINE,
}

data class BattleMode(
  val type: BattleModeType,
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
)

val battleModes = listOf(
  BattleMode(
    type = BattleModeType.ONLINE,
    title = "Play Online",
    subtitle = "Online mode",
    icon = Icons.Filled.Search,
  ),
  BattleMode(
    type = BattleModeType.OFFLINE,
    title = "Play with Computer",
    subtitle = "Offline mode",
    icon = Icons.Filled.Home,
  ),
)