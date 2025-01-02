package com.it1shka.checkers

import com.it1shka.checkers.gamelogic.Board
import com.it1shka.checkers.gamelogic.BotMinimax
import com.it1shka.checkers.gamelogic.BotRandom
import com.it1shka.checkers.gamelogic.CheckersBot
import com.it1shka.checkers.gamelogic.MinimaxConfigs
import com.it1shka.checkers.gamelogic.PieceColor
import org.junit.Test
import org.junit.Assert.*

/* Returns the winner color */
private fun imitateBattle(
  botBlack: CheckersBot,
  botRed: CheckersBot,
  log: Boolean = true,
): PieceColor {
  var board = Board.new()
  while (!board.possibleMoves.isEmpty()) {
    val move = if (board.turn == PieceColor.BLACK)
      botBlack.findMove(board)
      else botRed.findMove(board)
    if (move == null) {
      throw Exception("Critical error: move is null")
    }
    val nextBoard = board.makeMove(move)
    if (nextBoard == null) {
      throw Exception("Critical error: board is null")
    }
    board = nextBoard
    if (log) {
      println(board)
      println()
    }
  }
  return PieceColor.opposite(board.turn)
}

class BotTest {
  @Test
  fun `random bot should be making correct moves`() {
    var board = Board.new()
    val bot = BotRandom()
    while (!board.possibleMoves.isEmpty()) {
      val move = bot.findMove(board)
      if (move == null) {
        fail("Failed to get move")
        continue
      }
      val nextBoard = board.makeMove(move)
      if (nextBoard == null) {
        fail("Failed to perform move")
        continue
      }
      board = nextBoard
    }
  }

  @Test
  fun `minimax bot should be making correct moves`() {
    var board = Board.new()
    val botAdvanced = BotMinimax(MinimaxConfigs.normal)
    val botDefault = BotRandom()
    while (!board.possibleMoves.isEmpty()) {
      val move = if (board.turn == PieceColor.BLACK)
        botAdvanced.findMove(board)
        else botDefault.findMove(board)
      if (move == null) {
        fail("Failed to get move")
        continue
      }
      val nextBoard = board.makeMove(move)
      if (nextBoard == null) {
        fail("Failed to perform move")
        continue
      }
      board = nextBoard
      println(board)
      println()
    }
  }

  @Test
  fun `minimax bot should be making correct moves #2`() {
    var board = Board.new()
    val botAdvanced = BotMinimax(MinimaxConfigs.proficient)
    val botDefault = BotRandom()
    while (!board.possibleMoves.isEmpty()) {
      val move = if (board.turn == PieceColor.BLACK)
        botAdvanced.findMove(board)
      else botDefault.findMove(board)
      if (move == null) {
        fail("Failed to get move")
        continue
      }
      val nextBoard = board.makeMove(move)
      if (nextBoard == null) {
        fail("Failed to perform move")
        continue
      }
      board = nextBoard
      println(board)
      println()
    }
  }

  @Test(timeout = 20 * 1000)
  fun `testing the bot performance`() {
    try {
      imitateBattle(
        botRed = BotMinimax(MinimaxConfigs.strong),
        botBlack = BotRandom(),
      )
    } catch (_: Exception) {
      fail("Should not throw an exception")
    }
  }

  @Test
  fun `testing the correctness of the bot`() {
    repeat(100) {
      try {
        val result = imitateBattle(
          botBlack = BotMinimax(MinimaxConfigs.intermediate),
          botRed = BotRandom(),
        )
        if (result != PieceColor.BLACK) {
          fail("Lost to random bot")
        }
      } catch (_: Exception) {
        fail("Should not throw an exception")
      }
    }
  }
}