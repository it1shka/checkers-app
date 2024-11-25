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

@Composable
fun DifficultySelect(
  difficulty: String,
  changeDifficulty: (BotDifficulty) -> Unit,
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(10.dp),
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
      difficultyLevels.forEach {
        TextButton(
          onClick = { changeDifficulty(it.level) },
          colors =
          if (difficulty == it.level.name)
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
      text = difficultyLevels
        .find { it.level.name == difficulty }
        ?.description ?: "No description",
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}