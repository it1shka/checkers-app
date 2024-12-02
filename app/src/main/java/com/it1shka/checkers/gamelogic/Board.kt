package com.it1shka.checkers.gamelogic

import kotlin.math.abs

private data object BoardConstants {
  const val RED_PIECES_START = 1
  const val RED_PIECES_END = 12
  const val BLACK_PIECES_START = 21
  const val BLACK_PIECES_END = 32

  const val BOARD_START = 1
  const val BOARD_END = 8

  const val SYMBOL_BLACK_MAN = 'b'
  const val SYMBOL_BLACK_KING = 'B'
  const val SYMBOL_RED_MAN = 'r'
  const val SYMBOL_RED_KING = 'R'
  const val SYMBOL_EMPTY = ' '
  const val SYMBOL_SQUARE = '*'
}

val Pair<Square, Square>.isValidMove: Boolean
  get() {
    val (fromRow, fromCol) = this.first.position.value
    val (toRow, toCol) = this.second.position.value
    val rowDelta = abs(toRow - fromRow)
    val colDelta = abs(toCol - fromCol)
    return rowDelta == colDelta && rowDelta in 1..2
  }

@JvmInline
value class BoardMove(private val move: Pair<Square, Square>) {
  init {
    require(move.isValidMove) {
      "Move should be either normal move or a jump"
    }
  }

  val from: Square
    get() = move.first

  val to: Square
    get() = move.second

  val isJump: Boolean
    get() {
      val delta = abs(from.position.row - to.position.row)
      return delta == 2
    }

  val hitSquare: Square?
    get() {
      if (!isJump) return null
      val (row1, col1) = from.position.value
      val (row2, col2) = to.position.value
      val newRow = (row1 + row2) / 2
      val newCol = (col1 + col2) / 2
      return (newRow to newCol).asPosition?.square
    }
}

val Pair<Square, Square>.squarePairAsMove: BoardMove?
  get() = if (this.isValidMove)
    BoardMove(this)
    else null

val Pair<Int, Int>.intPairAsMove: BoardMove?
  get() {
    val (fromInt, toInt) = this
    val maybeFrom = fromInt.asSquare
    val maybeTo = toInt.asSquare
    if (maybeFrom == null || maybeTo == null) return null
    return (maybeFrom to maybeTo).squarePairAsMove
  }

private operator fun Pair<Int, Int>.times(scalar: Int): Pair<Int, Int> {
  val (row, col) = this
  return (row * scalar to col * scalar)
}

private operator fun Pair<Int, Int>.plus(another: Pair<Int, Int>): Pair<Int, Int> {
  val (row1, col1) = this
  val (row2, col2) = another
  return (row1 + row2 to col1 + col2)
}

data class Board (
  val turn: PieceColor,
  val pieces: List<Piece>,
  private val forcedJump: Square?
) {
  init {
    val taken = mutableSetOf<Square>()
    for (piece in pieces) {
      check(piece.square !in taken) {
        "Each square should be occupied by at most one piece"
      }
      taken += piece.square
    }
  }

  companion object {
    fun new(): Board {
      val redPieces = (BoardConstants.RED_PIECES_START..BoardConstants.RED_PIECES_END)
        .map {
          Piece(
            type = PieceType.MAN,
            color = PieceColor.RED,
            square = Square(it),
          )
        }
      val blackPieces = (BoardConstants.BLACK_PIECES_START..BoardConstants.BLACK_PIECES_END)
        .map {
          Piece(
            type = PieceType.MAN,
            color = PieceColor.BLACK,
            square = Square(it),
          )
        }
      return Board(
        turn = PieceColor.BLACK,
        pieces = redPieces + blackPieces,
        forcedJump = null,
      )
    }
  }

  fun pieceAt(square: Square): Piece? {
    return pieces.find { it.square == square }
  }

  private fun auxMovesAt(square: Square): List<BoardMove> {
    val piece = pieceAt(square)
    if (piece == null) return listOf()
    val directions = when {
      piece.type == PieceType.MAN && piece.color == PieceColor.BLACK ->
        listOf(-1 to -1, -1 to 1)
      piece.type == PieceType.MAN && piece.color == PieceColor.RED ->
        listOf(1 to -1, 1 to 1)
      else ->
        listOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1)
    }
    val current = piece.square.position.value
    val moves = mutableListOf<BoardMove>()
    var jump = false
    for (direction in directions) {
      val square = (current + direction).asPosition?.square
      if (square == null) continue
      val target = pieceAt(square)
      if (target == null) {
        moves += BoardMove(piece.square to square)
        continue
      }
      if (target.color == PieceColor.opposite(piece.color)) {
        val jumpSquare = (current + direction * 2).asPosition?.square
        if (jumpSquare == null || pieceAt(jumpSquare) != null) continue
        jump = true
        moves += BoardMove(piece.square to jumpSquare)
        continue
      }
    }
    return if (jump)
      moves.filter { it.isJump }
      else moves
  }

  val possibleMoves: List<BoardMove>
    get() {
      if (forcedJump != null) {
        return auxMovesAt(forcedJump)
          .filter { it.isJump }
      }
      val moves = pieces
        .filter { it.color == turn }
        .flatMap { auxMovesAt(it.square) }
      val jump = moves
        .find { it.isJump }
        .let { it != null }
      return if (jump)
        moves.filter { it.isJump }
        else moves
    }

  fun possibleMovesAt(square: Square): List<BoardMove> {
    return possibleMoves
      .filter { it.from == square }
  }

  fun makeMove(move: BoardMove): Board? = when {
    move !in possibleMoves -> null
    !move.isJump -> {
      val movingPiece = pieces.find { it.square == move.from }
      if (movingPiece == null) null
      else {
        val movedPiece = movingPiece.copy(square = move.to)
        copy(
          turn = PieceColor.opposite(turn),
          pieces = pieces
            .filter { it != movingPiece }
            .plus(movedPiece)
        )
      }
    }
    else -> {
      val movingPiece = pieces.find { it.square == move.from }
      val hitSquare = move.hitSquare
      if (movingPiece == null || hitSquare == null) null
      else {
        val movedPiece = movingPiece.copy(square = move.to)
        val nextBoard = copy(
          forcedJump = move.to,
          pieces = pieces
            .filter { it != movingPiece && it.square != hitSquare }
            .plus(movedPiece)
        )
        if (nextBoard.possibleMoves.isEmpty())
          nextBoard.copy (
            forcedJump = null,
            turn = PieceColor.opposite(turn),
          )
          else nextBoard
      }
    }
  }

  override fun toString() = buildString {
    for (row in BoardConstants.BOARD_START..BoardConstants.BOARD_END) {
      for (col in BoardConstants.BOARD_START..BoardConstants.BOARD_END) {
        val maybePosition = (row to col).asPosition
        if (maybePosition == null) {
          append(BoardConstants.SYMBOL_EMPTY)
          continue
        }
        val maybePiece = pieceAt(maybePosition.square)
        if (maybePiece == null) {
          append(BoardConstants.SYMBOL_SQUARE)
          continue
        }
        val symbol = when {
          maybePiece.type == PieceType.MAN && maybePiece.color == PieceColor.BLACK ->
            BoardConstants.SYMBOL_BLACK_MAN
          maybePiece.type == PieceType.KING && maybePiece.color == PieceColor.BLACK ->
            BoardConstants.SYMBOL_BLACK_KING
          maybePiece.type == PieceType.MAN && maybePiece.color == PieceColor.RED ->
            BoardConstants.SYMBOL_RED_MAN
          else ->
            BoardConstants.SYMBOL_RED_KING
        }
        append(symbol)
      }
      appendLine()
    }
    append("Turn: ${turn.name}")
    forcedJump?.let {
      appendLine()
      append("Forced jump: $forcedJump")
    }
  }
}
