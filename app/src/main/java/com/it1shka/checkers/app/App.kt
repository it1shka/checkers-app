package com.it1shka.checkers.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun App() {
  val navController = rememberNavController()
  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentScreen = remember(backStackEntry) {
    val currentRoute = backStackEntry?.destination?.route
    currentRoute?.let {
      AppScreen.valueOf(currentRoute)
    }
  }

  val routes = getRouting(navController)

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