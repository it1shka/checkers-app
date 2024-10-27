package com.it1shka.checkers.screens.battle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

private data class DifficultyLevel (
  val level: BotDifficulty,
  val title: String,
  val description: String,
)

private val levels = listOf(
  DifficultyLevel(
    level = BotDifficulty.EASY,
    title = "Easy",
    description = "Bot computes next 2 steps",
  ),
  DifficultyLevel(
    level = BotDifficulty.NORMAL,
    title = "Normal",
    description = "Bot computes next 4 steps",
  ),
  DifficultyLevel(
    level = BotDifficulty.HARD,
    title = "Hard",
    description = "Bot computes next 6 steps",
  ),
  DifficultyLevel(
    level = BotDifficulty.INSANE,
    title = "Insane",
    description = "Bot tries to compute all possible steps",
  ),
)

@Composable fun DifficultySelect(
  difficulty: BotDifficulty,
  changeDifficulty: (BotDifficulty) -> Unit,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      levels.forEach {
        TextButton (
          onClick = { changeDifficulty(it.level) },
          colors =
            if (difficulty == it.level)
              ButtonDefaults.filledTonalButtonColors()
            else
              ButtonDefaults.textButtonColors(),
          contentPadding = PaddingValues(all = 4.dp),
          content = {
            Text(it.title)
          }
        )
      }
    }
    Text(
      text = levels.find { it.level == difficulty }?.description ?: "No description",
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}