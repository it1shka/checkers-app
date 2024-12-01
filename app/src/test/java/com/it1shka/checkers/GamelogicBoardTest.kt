package com.it1shka.checkers

import com.it1shka.checkers.gamelogic.Board
import com.it1shka.checkers.gamelogic.Piece
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.gamelogic.PieceType
import com.it1shka.checkers.gamelogic.Square
import org.junit.Test
import org.junit.Assert.*

class BoardTest {
  @Test
  fun `correctly creates new board`() {
    val expected = listOf(
      " r r r r",
      "r r r r ",
      " r r r r",
      "* * * * ",
      " * * * *",
      "b b b b ",
      " b b b b",
      "b b b b ",
      "Turn: BLACK",
    ).joinToString(separator = "\n")
    val actual = Board.new().toString()
    assertEquals(expected, actual)
  }

  @Test
  fun `correctly picks pieces from the board`() {
    val testCases = listOf(
      1 to Piece(
        type = PieceType.MAN,
        color = PieceColor.RED,
        square = Square(1),
      ),
      6 to Piece(
        type = PieceType.MAN,
        color = PieceColor.RED,
        square = Square(6),
      ),
      13 to null,
      14 to null,
      18 to null,
      21 to Piece(
        type = PieceType.MAN,
        color = PieceColor.BLACK,
        square = Square(21),
      ),
      32 to Piece(
        type = PieceType.MAN,
        color = PieceColor.BLACK,
        square = Square(32),
      ),
    )
    val board = Board.new()
    for ((square, expected) in testCases) {
      val actual = board.pieceAt(square)
      assertEquals(expected, actual)
    }
  }
}