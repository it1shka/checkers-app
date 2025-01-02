package com.it1shka.checkers.gamelogic

import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

interface CheckersBot {
  val name: String
  fun findMove(board: Board): BoardMove?
}

object MinimaxConfigs {
  val normal = BotMinimaxConfig.default.copy(
    name = "normal",
    depth = 2,
  )
  val intermediate = BotMinimaxConfig.default.copy(
    name = "intermediate",
    depth = 4,
  )
  val strong = BotMinimaxConfig.default.copy(
    name = "strong",
    depth = 6,
  )
  val proficient = BotMinimaxConfig.default.copy(
    name = "proficient",
    depth = 9,
  )
  val random: BotMinimaxConfig
    get() = listOf(
      normal,
      intermediate,
      strong,
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
  val optimized: Boolean,
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
        optimized = true,
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
    return moves.maxBy { move ->
      val nextBoard = board.makeMove(move)
      if (nextBoard == null) Double.NEGATIVE_INFINITY
      else (
        if (config.optimized) minimaxOptimized(
          board = nextBoard,
          depth = config.depth,
          maximizing = board.turn,
          alpha = Double.NEGATIVE_INFINITY,
          beta = Double.POSITIVE_INFINITY,
        )
        else minimax(
          board = nextBoard,
          depth = config.depth,
          maximizing = board.turn,
        )
      )
    }
  }

  /* Classical version without alpha-beta pruning */
  private fun minimax(board: Board, depth: Int, maximizing: PieceColor): Double {
    if (depth <= 0) {
      return evaluateBoard(board, maximizing)
    }

    val moves = board.possibleMoves
    if (moves.isEmpty()) {
      return evaluateBoard(board, maximizing)
    }

    val scores = moves
      .map { board.makeMove(it) }
      .filterNotNull()
      .map { minimax(it, depth - 1, maximizing) }

    return if (board.turn == maximizing)
      scores.max()
    else scores.min()
  }

  /* Faster version with alpha-beta pruning */
  private fun minimaxOptimized(
    board: Board,
    depth: Int,
    maximizing: PieceColor,
    alpha: Double,
    beta: Double,
  ): Double {
    if (depth <= 0) {
      return evaluateBoard(board, maximizing)
    }

    val moves = board.possibleMoves
    if (moves.isEmpty()) {
      return evaluateBoard(board, maximizing)
    }

    val isMaximizing = maximizing == board.turn

    var runningAlpha = alpha
    var runningBeta = beta
    var score = if (isMaximizing)
      Double.NEGATIVE_INFINITY
      else Double.POSITIVE_INFINITY

    for (move in moves) {
      val nextBoard = board.makeMove(move)
      if (nextBoard == null) continue
      val currentScore = minimaxOptimized(
        board = nextBoard,
        depth = depth - 1,
        maximizing = maximizing,
        alpha = runningAlpha,
        beta = runningBeta,
      )
      score = if (isMaximizing)
        max(score, currentScore)
        else min(score, currentScore)
      if (isMaximizing) {
        if (score >= runningBeta) return score
        runningAlpha = max(runningAlpha, score)
      } else {
        if (score <= runningAlpha) return score
        runningBeta = min(runningBeta, score)
      }
    }
    return score
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