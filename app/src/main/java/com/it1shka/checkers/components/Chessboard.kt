package com.it1shka.checkers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

const val BOARD_SIZE = 8

data class ChessboardTheme(
  val blackCell: Color,
  val redCell: Color,
  val blackPiece: Color,
  val redPiece: Color,
  val highlight: Color,
)

private val defaultChessboardTheme = ChessboardTheme(
  blackCell = Color(0xffab47bc),
  redCell = Color(0xfff3e5f5),
  blackPiece = Color.Black,
  redPiece = Color(0xffff94b8),
  highlight = Color(0xff4287f5),
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

  val pieceModifier = Modifier
    .padding(4.dp)
    .fillMaxSize()
    .aspectRatio(1f)

  LazyVerticalGrid(
    columns = GridCells.Fixed(BOARD_SIZE),
    modifier = Modifier.aspectRatio(1f),
  ) {
    items(BOARD_SIZE * BOARD_SIZE) { index ->
      if (isSquare(index)) {
        val square = indexToSquare(index)
        Box(
          contentAlignment = Alignment.Center,
          modifier = baseModifier
            .background(if (square in highlight) theme.highlight else theme.blackCell)
            .clickable {
              onSquareClick(square)
            }
        ) {
          when (mappedState.get(square)) {
            SquareState.BLACK_MAN -> {
              Box(
                modifier = pieceModifier
                  .background(shape = CircleShape, color = theme.blackPiece)
              ) {}
            }

            SquareState.BLACK_KING -> {
              Box(
                modifier = pieceModifier
                  .background(shape = CircleShape, color = theme.blackPiece),
                contentAlignment = Alignment.Center,
              ) {
                Text(
                  text = "×",
                  style = MaterialTheme.typography.headlineSmall,
                  color = Color.White,
                )
              }
            }

            SquareState.RED_MAN -> {
              Box(
                modifier = pieceModifier
                  .background(shape = CircleShape, color = theme.redPiece)
              ) {}
            }

            SquareState.RED_KING -> {
              Box(
                modifier = pieceModifier
                  .background(shape = CircleShape, color = theme.redPiece),
                contentAlignment = Alignment.Center,
              ) {
                Text(
                  text = "×",
                  style = MaterialTheme.typography.headlineSmall,
                )
              }
            }

            else -> {}
          }
        }
      } else {
        Box(
          contentAlignment = Alignment.Center,
          modifier = baseModifier
            .background(theme.redCell)
        ) {}
      }
    }
  }
}