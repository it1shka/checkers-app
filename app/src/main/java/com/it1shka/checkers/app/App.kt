package com.it1shka.checkers.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.it1shka.checkers.Preferences
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.screens.battle.BotDifficulty

@Composable
fun App() {
  val context = LocalContext.current

  val navController = rememberNavController()
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentScreen = remember(backStackEntry) {
    val currentRoute = backStackEntry?.destination?.route
    currentRoute?.let {
      AppScreen.valueOf(currentRoute)
    }
  }

  val botDifficulty by Preferences
    .getDifficulty(context)
    .collectAsState(BotDifficulty.NORMAL.name)

  val playerColor by Preferences
    .getColor(context)
    .collectAsState(PieceColor.BLACK.name)

  val nickname by Preferences
    .getNickname(context)
    .collectAsState("AnonymousPlayer")

  val rating by Preferences
    .getRating(context)
    .collectAsState(0)

  val region by Preferences
    .getRegion(context)
    .collectAsState("Unknown")

  val routes = getRouting(navController, AppRouteArgs(
    difficulty = botDifficulty,
    color = playerColor,
    nickname = nickname,
    rating = rating,
    region = region,
  ))

  MaterialTheme {
    Scaffold(
      bottomBar = {
        AppNavbar(
          screen = currentScreen,
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
          composable(route.screen.name) {
            route.composable()
          }
        }
      }
    }
  }
}