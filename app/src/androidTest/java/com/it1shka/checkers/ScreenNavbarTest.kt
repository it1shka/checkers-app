package com.it1shka.checkers

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.it1shka.checkers.app.AppNavbar
import com.it1shka.checkers.app.AppScreen
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class ScreenNavbarTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun hasThreeDestinations() {
    val screen = mockk<AppScreen>()
    composeTestRule.setContent {
      AppNavbar(screen) {}
    }
    val destinations = listOf("Battle", "History", "Profile")
    for (dest in destinations) {
      composeTestRule
        .onNodeWithContentDescription(dest)
        .assertExists()
    }
  }
}