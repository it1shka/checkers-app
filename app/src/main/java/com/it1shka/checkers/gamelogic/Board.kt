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
}

val Pair<Square, Square>.asMove: BoardMove?
  get() = if (this.isValidMove)
    BoardMove(this)
    else null

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

  fun pieceAt(square: Int): Piece? {
    val maybeSquare = square.asSquare
    return when (maybeSquare) {
      is Square -> pieceAt(maybeSquare)
      else -> null
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
