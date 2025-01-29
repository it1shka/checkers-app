package com.it1shka.checkers.screens.online

import com.it1shka.checkers.gamelogic.GameStatus
import com.it1shka.checkers.gamelogic.PieceColor

val String.asColor get(): PieceColor {
  return if (this == "black") PieceColor.BLACK
    else PieceColor.RED
}

val GameStatus.winnerColor get(): PieceColor? {
  return when (this) {
    GameStatus.RED_WON -> PieceColor.RED
    GameStatus.BLACK_WON -> PieceColor.BLACK
    else -> null
  }
}