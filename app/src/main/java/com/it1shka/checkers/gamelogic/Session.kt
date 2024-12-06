package com.it1shka.checkers.gamelogic

private const val RULE_WITHOUT_MOVES = 40
private const val RULE_REPEAT_BOARD = 3

enum class GameStatus {
  ACTIVE,
  DRAW,
  BLACK_WON,
  RED_WON,
}

data class GameSession(
  val board: Board,
  private val cache: Map<Board, Int>,
  private val movesWithoutCapture: Int,
  private val history: List<Pair<Board, GameStatus>>
) {
  companion object {
    fun new(): GameSession {
      val newBoard = Board.new()
      return GameSession(
        board = newBoard,
        cache = mapOf(newBoard to 1),
        movesWithoutCapture = 0,
        history = listOf()
      )
    }
  }

  fun pieceAt(square: Square) =
    board.pieceAt(square)

  val possibleMoves: List<BoardMove>
    get() = board.possibleMoves

  fun possibleMovesAt(square: Square) =
    board.possibleMovesAt(square)

  val turn: PieceColor
    get() = board.turn

  val pieces: List<Piece>
    get() = board.pieces

  val status: GameStatus
    get() {
      if (board.possibleMoves.isEmpty()) {
        return when (board.turn) {
          PieceColor.RED -> GameStatus.BLACK_WON
          else -> GameStatus.RED_WON
        }
      }
      if (movesWithoutCapture >= RULE_WITHOUT_MOVES) {
        return GameStatus.DRAW
      }
      if (cache.values.any { it >= RULE_REPEAT_BOARD }) {
        return GameStatus.DRAW
      }
      return GameStatus.ACTIVE
    }

  val completeHistory: List<Pair<Board, GameStatus>>
    get() = history + (board to status)

  fun makeMove(move: BoardMove): GameSession? {
    if (status != GameStatus.ACTIVE) return null
    val nextBoard = board.makeMove(move)
    if (nextBoard == null) return null
    return GameSession(
      board = nextBoard,
      cache = cache +
        (nextBoard to cache.getOrDefault(nextBoard, 0) + 1),
      movesWithoutCapture = movesWithoutCapture +
        if (board.pieces.size == nextBoard.pieces.size) 1 else 0,
      history = completeHistory,
    )
  }
}