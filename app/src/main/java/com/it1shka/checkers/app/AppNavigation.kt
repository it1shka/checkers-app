package com.it1shka.checkers.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.it1shka.checkers.screens.battle.Battle
import com.it1shka.checkers.screens.offline.Offline
import com.it1shka.checkers.screens.online.Online
import com.it1shka.checkers.screens.profile.Profile

enum class AppScreen {
  BATTLE,
  OFFLINE_BATTLE,
  ONLINE_BATTLE,
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
  val nickname: String,
  val rating: Int,
  val region: String,
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
    Online(
      nickname = args.nickname,
      rating = args.rating,
      region = args.region,
    )
  }),
  AppRoute(AppScreen.HISTORY, {
    Column(modifier = Modifier.fillMaxSize()) {
      Text("TODO: ")
    }
  }),
  AppRoute(AppScreen.PROFILE, {
    Profile(
      initialNickname = args.nickname,
      initialRating = args.rating,
      initialRegion = args.region,
    )
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