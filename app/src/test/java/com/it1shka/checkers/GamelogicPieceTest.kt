package com.it1shka.checkers

import com.it1shka.checkers.gamelogic.Position
import com.it1shka.checkers.gamelogic.Square
import com.it1shka.checkers.gamelogic.asSquare
import com.it1shka.checkers.gamelogic.isValidPosition
import org.junit.Test
import org.junit.Assert.*

class SquareTest {
  @Test
  fun `creating valid square returns success`() {
    for (i in 1..32) {
      try {
        Square(i)
      } catch (_: Exception) {
        fail("Square $i should be valid")
      }
    }
  }

  @Test
  fun `creating invalid square throws exception`() {
    for (i in (-10..0) + (33 .. 50)) {
      assertThrows(IllegalArgumentException::class.java) {
        Square(i)
      }
    }
  }

  @Test
  fun `converting square to position should return a valid pair`() {
    val testCases = listOf(
      1 to (1 to 2),
      2 to (1 to 4),
      10 to (3 to 4),
      12 to (3 to 8),
      14 to (4 to 3),
      19 to (5 to 6),
      21 to (6 to 1),
      27 to (7 to 6),
      29 to (8 to 1),
      32 to (8 to 7),
    )
    for ((square, expected) in testCases) {
      val actual = Square(square).position.value
      assertEquals(expected, actual)
    }
  }

  @Test
  fun `each square should be invertible`() {
    val testCases = listOf(
      1 to 32,
      9 to 24,
      10 to 23,
      13 to 20,
    )
    for ((rawSquare, rawInvertedSquare) in testCases) {
      val square = rawSquare.asSquare
      val invertedSquare = rawInvertedSquare.asSquare
      if (square == null || invertedSquare == null) {
        fail("valid squares are null after the conversion")
        return
      }
      assertEquals(square.inverse, invertedSquare)
    }
  }
}

class PositionTest {
  @Test
  fun `creating valid position should return success`() {
    for (i in 1..8) {
      for (j in 1..8) {
        if (i % 2 == j % 2) continue
        try {
          Position(i to j)
        } catch (_: Exception) {
          fail("Position $i, $j should be valid")
        }
      }
    }
  }

  @Test
  fun `creating invalid position should throw`() {
    val testCases = listOf(
      1 to 1,
      1 to 3,
      3 to 1,
      9 to 9,
      -1 to 2,
      0 to 0,
      0 to 1,
      6 to 4,
    )
    for ((row, col) in testCases) {
      assertThrows(IllegalArgumentException::class.java) {
        Position(row to col)
      }
    }
  }

  @Test
  fun `position should be convertible to a proper square`() {
    val testCases = listOf(
      (1 to 2) to 1,
      (1 to 8) to 4,
      (3 to 4) to 10,
      (4 to 5) to 15,
      (6 to 3) to 22,
      (7 to 6) to 27,
      (8 to 3) to 30,
    )
    for ((position, expected) in testCases) {
      val actual = Position(position).square.value
      assertEquals(expected, actual)
    }
  }

  @Test
  fun `valid positions should return true when checked`() {
    val validPositions = listOf(
      1 to 2,
      1 to 4,
      1 to 6,
      4 to 3,
      4 to 7,
      6 to 5,
    )
    for (pos in validPositions) {
      assertTrue(pos.isValidPosition)
    }
  }
}