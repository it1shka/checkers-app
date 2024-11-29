package com.it1shka.checkers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

const val BOARD_SIZE = 8

data class ChessboardTheme (
  val black: Color,
  val red: Color,
)

private val defaultChessboardTheme = ChessboardTheme(
  black = Color(0xffab47bc),
  red = Color(0xfff3e5f5),
)

enum class SquareState {
  EMPTY,
  BLACK_MAN,
  RED_MAN,
  BLACK_KING,
  RED_KING,
}

private fun isSquare(index: Int): Boolean {
  val row = index / 8
  val rem = index % 2
  return if (row % 2 == 0) rem != 0 else rem == 0
}

private fun indexToSquare(index: Int): Int {
  return index / 2 + 1;
}

@Composable
fun Chessboard(
  state: Iterable<Pair<Int, SquareState>> = emptyList(),
  highlight: Iterable<Int> = emptyList(),
  onSquareClick: (Int) -> Unit = { },
  theme: ChessboardTheme = defaultChessboardTheme,
) {
  val mappedState = remember(state) { state.toMap() }

  val baseModifier = Modifier
    .aspectRatio(1f)

  LazyVerticalGrid(
    columns = GridCells.Fixed(BOARD_SIZE),
    modifier = Modifier.aspectRatio(1f),
  ) {
    items(BOARD_SIZE * BOARD_SIZE) { index ->
      if (isSquare(index)) {
        Box(
          contentAlignment = Alignment.Center,
          modifier = baseModifier
            .background(theme.black)
            .clickable {
              onSquareClick(indexToSquare(index))
            }
        ) {}
      } else {
        Box(
          contentAlignment = Alignment.Center,
          modifier = baseModifier
            .background(theme.red)
        ) {}
      }
    }
  }
}