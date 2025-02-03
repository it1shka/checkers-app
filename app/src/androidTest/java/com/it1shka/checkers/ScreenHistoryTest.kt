package com.it1shka.checkers

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.it1shka.checkers.data.Enemy
import com.it1shka.checkers.data.GameEntity
import com.it1shka.checkers.data.PersistViewModel
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.screens.history.History
import com.it1shka.checkers.screens.history.HistoryViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ScreenHistoryTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun noRecordsResultsInMessage() {
    val nav = mockk<NavController>(relaxed = true)
    val persistViewModel = mockk<PersistViewModel>()
    val historyViewModel = mockk<HistoryViewModel>()

    every { persistViewModel.games } returns emptyFlow()

    composeTestRule.setContent {
      History(nav, persistViewModel, historyViewModel)
    }
    composeTestRule.onNodeWithText("No battles played")
      .assertIsDisplayed()
  }

  @Test
  fun correctlyDisplaysRecords() {
    val nav = mockk<NavController>(relaxed = true)
    val persistViewModel = mockk<PersistViewModel>()
    val historyViewModel = mockk<HistoryViewModel>()

    val nicknames = listOf("nickname-1", "nickname-2", "nickname-3")

    every { persistViewModel.games } returns flowOf(
      nicknames.map {
        GameEntity(
          id = "1",
          playerColor = PieceColor.BLACK.name,
          enemy = Enemy(nickname = it),
          playedAt = System.currentTimeMillis(),
        )
      }
    )

    composeTestRule.setContent {
      History(nav, persistViewModel, historyViewModel)
    }

    for (nickname in nicknames) {
      val nodeText = "Battle with $nickname"
      val node = composeTestRule.onNodeWithText(nodeText)
      node.assertIsDisplayed()
    }
  }

  @Test
  fun displayedRecordsAreClickable() {
    val nav = mockk<NavController>(relaxed = true)
    val persistViewModel = mockk<PersistViewModel>()
    val historyViewModel = mockk<HistoryViewModel>()

    val nicknames = listOf("nickname-1", "nickname-2", "nickname-3")

    every { persistViewModel.games } returns flowOf(
      nicknames.map {
        GameEntity(
          id = "1",
          playerColor = PieceColor.BLACK.name,
          enemy = Enemy(nickname = it),
          playedAt = System.currentTimeMillis(),
        )
      }
    )

    composeTestRule.setContent {
      History(nav, persistViewModel, historyViewModel)
    }

    for (nickname in nicknames) {
      val nodeText = "Battle with $nickname"
      val node = composeTestRule.onNodeWithText(nodeText)
      node.assertHasClickAction()
    }
  }
}