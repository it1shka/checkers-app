package com.it1shka.checkers

import com.it1shka.checkers.gamelogic.Board
import com.it1shka.checkers.gamelogic.intPairAsMove
import org.junit.Test
import org.junit.Assert.*

class BoardPlayTest {
  @Test
  fun `should be able to have a basic game`() {
    val moves = listOf(
      21 to 17,
      10 to 14,
      17 to 10,
      7 to 14,
      24 to 19,
      11 to 15,
      19 to 10,
    )
    var board = Board.new();
    for (maybeMove in moves) {
      val move = maybeMove.intPairAsMove
      if (move == null) {
        fail("$maybeMove should be a valid move")
        continue
      }
      val maybeNextBoard = board.makeMove(move)
      if (maybeNextBoard == null) {
        fail("$move failed")
        continue
      }
      board = maybeNextBoard
    }
    val expected = listOf(
      " r r r r",
      "r r * r ",
      " r b * r",
      "* r * * ",
      " * * * *",
      "* b b * ",
      " b b b b",
      "b b b b ",
      "Turn: RED"
    ).joinToString("\n")
    assertEquals(expected, board.toString())
  }
}