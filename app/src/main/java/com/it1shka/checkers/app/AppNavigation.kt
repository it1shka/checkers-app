package com.it1shka.checkers.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.it1shka.checkers.screens.battle.Battle
import com.it1shka.checkers.screens.offline.Offline

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

data class AppRouteArgs(
  val difficulty: String,
  val color: String,
)

fun getRouting(navController: NavController, args: AppRouteArgs) = listOf(
  AppRoute(AppScreen.BATTLE, {
    Battle(
      nav = navController,
      initialDifficulty = args.difficulty,
    )
  }),
  AppRoute(AppScreen.OFFLINE_BATTLE, {
    Offline(
      nav = navController,
      initialDifficulty = args.difficulty,
      initialColor = args.color,
    )
  }),
  AppRoute(AppScreen.ONLINE_BATTLE, {
    Column(modifier = Modifier.fillMaxSize()) {
      Text("TODO: ")
    }
  }),
  AppRoute(AppScreen.HALL_OF_FAME, {
    Column(modifier = Modifier.fillMaxSize()) {
      Text("TODO: ")
    }
  }),
  AppRoute(AppScreen.HISTORY, {
    Column(modifier = Modifier.fillMaxSize()) {
      Text("TODO: ")
    }
  }),
  AppRoute(AppScreen.PROFILE, {
    Column(modifier = Modifier.fillMaxSize()) {
      Text("TODO: ")
    }
  })
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