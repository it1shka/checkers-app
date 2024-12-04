package com.it1shka.checkers

import com.it1shka.checkers.gamelogic.Board
import com.it1shka.checkers.gamelogic.BotMinimax
import com.it1shka.checkers.gamelogic.BotRandom
import com.it1shka.checkers.gamelogic.MinimaxConfigs
import org.junit.Test
import org.junit.Assert.*

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
    var index = 0
    while (!board.possibleMoves.isEmpty()) {
      val move = if (index % 2 == 0)
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
      index++
      println(board)
      println()
    }
  }

  @Test
  fun `testing the bot accuracy and performance`() {
    var board = Board.new()
    val botAdvanced = BotMinimax(MinimaxConfigs.strong)
    val botDefault = BotRandom()
    var index = 0
    while (!board.possibleMoves.isEmpty()) {
      val move = if (index % 2 == 0)
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
      index++
      println(board)
      println()
    }
  }
}