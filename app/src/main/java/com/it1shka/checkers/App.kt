package com.it1shka.checkers

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.it1shka.checkers.screens.battle.Battle
import com.it1shka.checkers.screens.history.History
import com.it1shka.checkers.screens.offline.OfflineBattle
import com.it1shka.checkers.screens.online.OnlineBattle
import com.it1shka.checkers.screens.profile.Profile
import com.it1shka.checkers.screens.ranking.Ranking

enum class AppScreen {
  BATTLE,
  OFFLINE_BATTLE,
  ONLINE_BATTLE,
  HALL_OF_FAME,
  HISTORY,
  PROFILE,
}

private data class Route (
  val screen: AppScreen,
  val layout: @Composable () -> Unit,
)

@Composable fun App() {
  val navController = rememberNavController()
  val backStackEntry by navController.currentBackStackEntryAsState()

  val screen = remember(backStackEntry) {
    val route = backStackEntry?.destination?.route
    if (route == null) null
    else when {
      route in AppScreen.entries.map { it.name } ->
        AppScreen.valueOf(route)
      else ->
        null
    }
  }

  val routes = listOf(
    Route(AppScreen.BATTLE, { Battle(navController) }),
    Route(AppScreen.OFFLINE_BATTLE, { OfflineBattle(navController) }),
    Route(AppScreen.ONLINE_BATTLE, { OnlineBattle() }),
    Route(AppScreen.HALL_OF_FAME, { Ranking() }),
    Route(AppScreen.HISTORY, { History() }),
    Route(AppScreen.PROFILE, { Profile() }),
  )

  MaterialTheme {
    Scaffold(
      bottomBar = {
        Navbar(
          screen = screen,
          changeScreen = { navController.navigate(it.name) }
        )
      }
    ) { innerPadding ->
      NavHost(
        navController = navController,
        startDestination = AppScreen.BATTLE.name,
        modifier = Modifier.padding(innerPadding),
        enterTransition = {
          slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down)
        },
        exitTransition = {
          slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down)
        }
      ) {
        routes.forEach { route ->
          composable(route.screen.name) { route.layout() }
        }
      }
    }
  }
}