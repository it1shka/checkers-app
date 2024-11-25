package com.it1shka.checkers.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppScreen {
  BATTLE,
  OFFLINE_BATTLE,
  ONLINE_BATTLE,
  HALL_OF_FAME,
  HISTORY,
  PROFILE,
}

data class AppRoute(
  val screen: AppScreen,
  val composable: @Composable () -> Unit
)

data class NavbarItem(
  val screen: AppScreen,
  val title: String,
  val selectedIcon: ImageVector,
  val regularIcon: ImageVector,
)

val navbarItems = listOf(
  NavbarItem(
    screen = AppScreen.BATTLE,
    title = "Battle",
    selectedIcon = Icons.Filled.Star,
    regularIcon = Icons.Outlined.Star,
  ),
  NavbarItem(
    screen = AppScreen.HALL_OF_FAME,
    title = "Hall of Fame",
    selectedIcon = Icons.Filled.ThumbUp,
    regularIcon = Icons.Outlined.ThumbUp,
  ),
  NavbarItem(
    screen = AppScreen.HISTORY,
    title = "History",
    selectedIcon = Icons.Filled.DateRange,
    regularIcon = Icons.Outlined.DateRange,
  ),
  NavbarItem(
    screen = AppScreen.PROFILE,
    title = "Profile",
    selectedIcon = Icons.Filled.AccountCircle,
    regularIcon = Icons.Outlined.AccountCircle,
  )
)