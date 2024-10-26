package com.it1shka.checkers

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.it1shka.checkers.screens.battle.Battle
import com.it1shka.checkers.screens.history.History
import com.it1shka.checkers.screens.profile.Profile
import com.it1shka.checkers.screens.ranking.Ranking

enum class AppScreen {
  BATTLE,
  HALL_OF_FAME,
  HISTORY,
  PROFILE,
}

private data class Route (
  val screen: AppScreen,
  val layout: @Composable () -> Unit,
)

private val routes = listOf(
  Route(AppScreen.BATTLE, { Battle() }),
  Route(AppScreen.HALL_OF_FAME, { Ranking() }),
  Route(AppScreen.HISTORY, { History() }),
  Route(AppScreen.PROFILE, { Profile() }),
)

@Composable fun App() {
  val navController = rememberNavController()
  var screen by remember { mutableStateOf(AppScreen.BATTLE) }

  LaunchedEffect(screen) {
    navController.navigate(screen.name)
  }

  MaterialTheme {
    Scaffold(
      bottomBar = {
        Navbar(
          screen = screen,
          changeScreen = { screen = it }
        )
      }
    ) { innerPadding ->
      NavHost(
        navController = navController,
        startDestination = AppScreen.BATTLE.name,
        modifier = Modifier.padding(innerPadding)
      ) {
        routes.forEach { route ->
          composable(route.screen.name) { route.layout() }
        }
      }
    }
  }
}