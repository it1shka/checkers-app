package com.it1shka.checkers

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import com.it1shka.checkers.screens.profile.Profile
import org.junit.Rule
import org.junit.Test

class ScreenProfileTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun badgePyramidDisplayedCorrectly() {
    val pyramid = listOf(
      "Novice",
      "Novice+",
      "Medium",
      "Medium+",
      "Master",
      "Legend",
      "Champion"
    )

    composeTestRule.setContent {
      Profile("nickname-1", 100, "Poland")
    }

    for (badge in pyramid) {
      composeTestRule
        .onAllNodesWithText(badge)
        .get(0)
        .assertIsDisplayed()
    }
  }
}