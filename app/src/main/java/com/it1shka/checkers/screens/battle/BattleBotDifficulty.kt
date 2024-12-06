package com.it1shka.checkers.screens.battle

enum class BotDifficulty {
  EASY,
  NORMAL,
  HARD,
  INSANE,
}

data class DifficultyLevel(
  val level: BotDifficulty,
  val title: String,
  val description: String,
)

val difficultyLevels = listOf(
  DifficultyLevel(
    level = BotDifficulty.EASY,
    title = "Easy",
    description = "Bot makes random moves",
  ),
  DifficultyLevel(
    level = BotDifficulty.NORMAL,
    title = "Normal",
    description = "Bot computes next 2 steps",
  ),
  DifficultyLevel(
    level = BotDifficulty.HARD,
    title = "Hard",
    description = "Bot computes next 4 steps",
  ),
  DifficultyLevel(
    level = BotDifficulty.INSANE,
    title = "Insane",
    description = "Bot computes next 6 steps",
  ),
)