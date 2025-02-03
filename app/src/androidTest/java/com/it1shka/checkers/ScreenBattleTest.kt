package com.it1shka.checkers

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.it1shka.checkers.screens.battle.Battle
import com.it1shka.checkers.screens.battle.BotDifficulty
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class ScreenBattleTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun hasAppTitle() {
    val nav = mockk<NavController>()
    composeTestRule.setContent {
      Battle(nav, BotDifficulty.NORMAL.name)
    }
    val titleNode = composeTestRule.onNodeWithText("Checkers Online")
    titleNode.assertExists().assertIsDisplayed()
  }

  @Test
  fun twoBattleModes() {
    val nav = mockk<NavController>()
    composeTestRule.setContent {
      Battle(nav, BotDifficulty.NORMAL.name)
    }
    val modes = listOf("Play Online", "Play with Computer")
    for (mode in modes) {
      val node = composeTestRule.onNodeWithText(mode)
      node.assertIsDisplayed()
      node.assertHasClickAction()
    }
  }

  @Test
  fun fourDifficulties() {
    val nav = mockk<NavController>()
    composeTestRule.setContent {
      Battle(nav, BotDifficulty.NORMAL.name)
    }
    val difficulties = listOf("Easy", "Normal", "Hard", "Insane")
    for (difficulty in difficulties) {
      val node = composeTestRule.onNodeWithText(difficulty)
      node.assertIsDisplayed()
      node.assertHasClickAction()
    }
  }
}