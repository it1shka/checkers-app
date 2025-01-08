package com.it1shka.checkers.screens.online

import com.it1shka.checkers.gamelogic.PieceColor

val String.asColor get(): PieceColor {
  return if (this == "black") PieceColor.BLACK
    else PieceColor.RED
}