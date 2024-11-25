package com.it1shka.checkers.app

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun AppNavbar(screen: AppScreen, changeScreen: (AppScreen) -> Unit) {
  val visible = remember(screen) {
    screen in navbarItems.map { it.screen }
  }

  if (!visible) {
    return
  }

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