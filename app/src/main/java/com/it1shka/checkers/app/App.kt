package com.it1shka.checkers.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.it1shka.checkers.screens.battle.Battle
import com.it1shka.checkers.screens.offline.Offline

@Composable
fun App(viewModel: AppViewModel = viewModel()) {
  val navController = rememberNavController()
  val screen by viewModel.screen.collectAsState()
  LaunchedEffect(screen) {
    navController.navigate(screen.name)
  }

  // TODO: complete the routing
  val routes = listOf(
    AppRoute(AppScreen.BATTLE, {
      Battle { viewModel.navigateTo(it) }
    }),
    AppRoute(AppScreen.OFFLINE_BATTLE, {
      Offline { viewModel.navigateTo(it) }
    }),
    AppRoute(AppScreen.ONLINE_BATTLE, {
      Text("TODO: ")
    }),
    AppRoute(AppScreen.HALL_OF_FAME, {
      Text("TODO: ")
    }),
    AppRoute(AppScreen.HISTORY, {
      Text("TODO: ")
    }),
    AppRoute(AppScreen.PROFILE, {
      Text("TODO: ")
    })
  )

  MaterialTheme {
    Scaffold(
      bottomBar = {
        AppNavbar(
          screen = screen,
          changeScreen = { viewModel.navigateTo(it) }
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