package com.it1shka.checkers

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.it1shka.checkers.app.AppNavbar
import com.it1shka.checkers.app.AppScreen
import org.junit.Rule
import org.junit.Test

class ScreenNavbarTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun hasThreeDestinations() {
    composeTestRule.setContent {
      AppNavbar(AppScreen.BATTLE) {}
    }
    composeTestRule.onRoot().printToLog("TREE")
    val destinations = listOf("Battle", "History", "Profile")
    for (dest in destinations) {
      composeTestRule
        .onNodeWithText(dest)
        .assertExists()
    }
  }

  @Test
  fun destinationsAreClickable() {
    composeTestRule.setContent {
      AppNavbar(AppScreen.BATTLE) {}
    }
    val destinations = listOf("Battle", "History", "Profile")
    for (dest in destinations) {
      val node = composeTestRule.onNodeWithText(dest)
      node.assertHasClickAction()
    }
  }
}