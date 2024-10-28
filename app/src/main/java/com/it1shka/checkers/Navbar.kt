package com.it1shka.checkers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

private data class NavbarItem (
  val screen: AppScreen,
  val title: String,
  val selectedIcon: ImageVector,
  val regularIcon: ImageVector,
)

private val navbarItems = listOf(
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

@Composable fun Navbar(screen: AppScreen?, changeScreen: (AppScreen) -> Unit) {
  NavigationBar {
    navbarItems.forEach {
      NavigationBarItem(
        icon = {
          Icon(
            imageVector = if (it.screen == screen) {
              it.selectedIcon
            } else {
              it.regularIcon
            },
            contentDescription = it.title,
          )
        },
        label = { Text(it.title) },
        selected = it.screen == screen,
        onClick = { changeScreen(it.screen) }
      )
    }
  }
}