package com.it1shka.checkers

import com.it1shka.checkers.gamelogic.Board
import com.it1shka.checkers.gamelogic.BoardMove
import com.it1shka.checkers.gamelogic.BotRandom
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
      val actual = board.pieceAt(Square(square))
      assertEquals(expected, actual)
    }
  }

  @Test
  fun `hashCode for equal boards should be equal`() {
    val board1 = Board.new()
    val board2 = Board.new()
    assertTrue(board1.hashCode() == board2.hashCode())
  }

  @Test
  fun `testing hashCode for random moves`() {
    var board1 = Board.new()
    var board2 = Board.new()
    var bot = BotRandom()
    run iterations@ {
      repeat(1000) {
        val move = bot.findMove(board1)
        if (move == null) return@iterations
        board1 = board1.makeMove(move)!!
        board2 = board2.makeMove(move)!!
        assertTrue(board1.hashCode() == board2.hashCode())
      }
    }
  }

  @Test
  fun `hashCode should be different for different board`() {
    val board1 = Board.new()
    val board2 = Board.new().makeMove(BoardMove(Square(1) to Square(6)))
    assertFalse(board1.hashCode() == board2.hashCode())
  }
}

class BoardMoveTest {
  @Test
  fun `correctly creates valid moves`() {
    val testCases = listOf(
      1 to 6,
      2 to 6,
      10 to 19,
      23 to 16,
      32 to 23,
    )
    for ((from, to) in testCases) {
      try {
        BoardMove(Square(from) to Square(to))
      } catch (_: Exception) {
        fail("$from -> $to should be a valid move")
      }
    }
  }

  @Test
  fun `throws exception for invalid moves`() {
    val testCases = listOf(
      1 to 15,
      1 to 4,
      18 to 19,
      32 to 18,
      14 to 22,
    )
    for ((from, to) in testCases) {
      assertThrows(IllegalArgumentException::class.java) {
        BoardMove(Square(from) to Square(to))
      }
    }
  }

  @Test
  fun `detects when move is not a jump`() {
    val testCases = listOf(
      24 to 20,
      13 to 9,
      3 to 7,
      15 to 18,
    )
    for ((from, to) in testCases) {
      val actual = BoardMove(Square(from) to Square(to)).isJump
      assertEquals(false, actual)
    }
  }

  @Test
  fun `detects when move is indeed a jump`() {
    val testCases = listOf(
      1 to 10,
      2 to 9,
      19 to 10,
      22 to 15,
      22 to 31,
    )
    for ((from, to) in testCases) {
      val actual = BoardMove(Square(from) to Square(to)).isJump
      assertEquals(true, actual)
    }
  }
}