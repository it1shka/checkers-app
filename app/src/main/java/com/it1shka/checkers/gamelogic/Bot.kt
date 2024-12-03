package com.it1shka.checkers.gamelogic

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

interface CheckersBot {
  val name: String
  fun findMove(board: Board): BoardMove?
}

object MinimaxConfigs {
  val default = BotMinimaxConfig.default
  val intermediate = BotMinimaxConfig.default.copy(
    name = "intermediate",
    depth = 6,
  )
  val advanced = BotMinimaxConfig.default.copy(
    name = "advanced",
    depth = 9,
  )
  val proficient = BotMinimaxConfig.default.copy(
    name = "proficient",
    depth = 12,
  )
  val random: BotMinimaxConfig
    get() = listOf(
      default, intermediate,
      advanced, proficient
    ).random()
}

class BotRandom : CheckersBot {
  override val name: String
    get() = "Random"

  override fun findMove(board: Board): BoardMove? {
    val moves = board.possibleMoves
    if (moves.isEmpty()) return null
    val idx = Random.nextInt(moves.size)
    return moves[idx]
  }
}

data class BotMinimaxConfig(
  val name: String,
  val depth: Int,
  val winWeight: Double,
  val manWeight: Double,
  val kingWeight: Double,
  val centerBonus: Double,
  val edgeBonus: Double,
  val backRankBonus: Double,
) {
  companion object {
    val default: BotMinimaxConfig
      get() = BotMinimaxConfig(
        name = "default",
        depth = 3,
        winWeight = Double.POSITIVE_INFINITY,
        manWeight = 1.0,
        kingWeight = 2.0,
        centerBonus = 0.25,
        edgeBonus = 0.25,
        backRankBonus = 0.5,
      )
  }
}

class BotMinimax(private val config: BotMinimaxConfig = BotMinimaxConfig.default) : CheckersBot {
  companion object {
    private val centerSquares = listOf(
      10, 11, 14, 15,
      18, 19, 22, 23,
    ).map { Square(it) }

    private val edgeSquares = listOf(
      1, 2, 3, 4,
      5, 13, 21, 29,
      12, 20, 28,
      30, 31, 32,
    ).map { Square(it) }

    private val blackBackRank = listOf(
      5, 6, 7, 8,
    ).map { Square(it) }

    private val redBackRank = listOf(
      25, 26, 27, 28,
    ).map { Square(it) }
  }

  override val name: String
    get() = "Minimax-${config.name}"

  override fun findMove(board: Board): BoardMove? {
    val moves = board.possibleMoves.shuffled()
    if (moves.isEmpty()) return null
    var bestMove = moves.first()
    var bestScore = Double.NEGATIVE_INFINITY
    for (move in moves) {
      val nextBoard = board.makeMove(move)
      if (nextBoard == null) continue
      val score = minimax(
        nextBoard,
        config.depth,
        Double.NEGATIVE_INFINITY,
        Double.POSITIVE_INFINITY,
        board.turn,
      )
      if (score > bestScore) {
        bestScore = score
        bestMove = move
      }
    }
    return bestMove
  }

  private fun minimax(
    board: Board,
    depth: Int,
    alpha: Double,
    beta: Double,
    maximizing: PieceColor,
  ): Double {
    if (depth <= 0) {
      return evaluateBoard(board, maximizing)
    }
    val moves = board.possibleMoves
    if (moves.isEmpty()) {
      return evaluateBoard(board, maximizing)
    }
    var output = if (board.turn == maximizing)
      Double.NEGATIVE_INFINITY
    else Double.POSITIVE_INFINITY
    var localAlpha = alpha
    var localBeta = beta
    for (move in moves) {
      val nextBoard = board.makeMove(move)
      if (nextBoard == null) continue
      val score = minimax(nextBoard, depth - 1, localAlpha, localBeta, maximizing)
      output = if (board.turn == maximizing)
        max(output, score)
      else min(output, score)
      if (board.turn == maximizing) {
        localAlpha = max(localAlpha, score)
      } else {
        localBeta = min(localBeta, score)
      }
      if (localBeta <= localAlpha) break
    }
    return output
  }

  private fun evaluateBoard(board: Board, color: PieceColor): Double {
    if (board.possibleMoves.isEmpty()) {
      return if (board.turn == color)
        -config.winWeight
      else config.winWeight
    }
    return board.pieces.sumOf { piece ->
      val coefficient = if (piece.color == color) 1.0 else -1.0
      var local = when (piece.type) {
        PieceType.MAN -> config.manWeight
        else -> config.kingWeight
      }
      if (piece.square in centerSquares) {
        local += config.centerBonus
      }
      if (piece.square in edgeSquares) {
        local += config.edgeBonus
      }
      val backRank = if (piece.color == PieceColor.BLACK)
        blackBackRank
      else redBackRank
      if (piece.square in backRank) {
        local += config.backRankBonus
      }
      local * coefficient
    }
  }
}