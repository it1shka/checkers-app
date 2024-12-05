package com.it1shka.checkers.gamelogic

private data object PieceConstants {
  const val SQUARE_START = 1
  const val SQUARE_END = 32
  const val BOARD_START = 1
  const val BOARD_END = 8
}

val Int.isValidSquare: Boolean
  get() {
    val squareRange = PieceConstants.SQUARE_START..PieceConstants.SQUARE_END
    return this in squareRange
  }

@JvmInline
value class Square(private val square: Int) {
  init {
    require(square.isValidSquare) {
      "Square should be between 1 and 32"
    }
  }

  val value: Int
    get() = square

  val position: Position
    get() {
      val row = (square + 3) / 4
      val column = if (row % 2 == 0) {
        (square - (row - 1) * 4) * 2 - 1
      } else {
        (square - (row - 1) * 4) * 2
      }
      return Position(row to column)
    }
}

val Int.asSquare: Square?
  get() = if (this.isValidSquare)
    Square(this)
  else null

val Pair<Int, Int>.isValidPosition: Boolean
  get() {
    val (row, col) = this
    val boardRange = PieceConstants.BOARD_START..PieceConstants.BOARD_END
    return (row in boardRange) && (col in boardRange) && (row % 2 != col % 2)
  }

@JvmInline
value class Position(private val position: Pair<Int, Int>) {
  init {
    require(position.isValidPosition) {
      "Position should have row, column in the range [1, 8] and be a black square"
    }
  }

  val value: Pair<Int, Int>
    get() = position

  val row: Int
    get() = position.first

  val column: Int
    get() = position.second

  val square: Square
    get() = Square((row - 1) * 4 + (column + 1) / 2)
}

val Pair<Int, Int>.asPosition: Position?
  get() = if (this.isValidPosition)
    Position(this)
  else null

enum class PieceType {
  MAN,
  KING,
}

enum class PieceColor {
  BLACK,
  RED;

  companion object {
    fun opposite(color: PieceColor) = when (color) {
      BLACK -> RED
      else -> BLACK
    }
  }
}

data class Piece(
  val type: PieceType,
  val color: PieceColor,
  val square: Square,
)