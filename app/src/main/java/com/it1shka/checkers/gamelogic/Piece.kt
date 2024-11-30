package com.it1shka.checkers.gamelogic

@JvmInline
value class Square(private val square: Int) {
  init {
    require(square in 1..32) {
      "Square should be between 1 and 32"
    }
  }

  val value: Int
    get() = square

  val position: Position
    get() {
      val row = (square + 3) / 4
      val column = if (row % 2 == 0) {
        (square - (row -1) * 4) * 2 - 1
      } else {
        (square - (row -1) * 4) * 2
      }
      return Position(row to column)
    }
}

@JvmInline
value class Position(private val position: Pair<Int, Int>) {
  init {
    val (row, col) = position
    require(row in 1..8 && col in 1..8) {
      "Position should have row, column in the range [1, 8]"
    }
    require(row % 2 != col % 2) {
      "Position should correspond to a valid black square on the board"
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