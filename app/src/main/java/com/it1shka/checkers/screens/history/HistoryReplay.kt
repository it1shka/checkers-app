package com.it1shka.checkers.screens.history

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.it1shka.checkers.components.Chessboard
import com.it1shka.checkers.components.SquareState
import com.it1shka.checkers.data.PersistViewModel
import com.it1shka.checkers.gamelogic.Board
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.gamelogic.PieceType
import com.it1shka.checkers.gamelogic.Square
import com.it1shka.checkers.gamelogic.asSquare
import com.it1shka.checkers.gamelogic.squarePairAsMove
import kotlinx.coroutines.flow.flowOf

@Composable
fun HistoryReplay(
  persistViewModel: PersistViewModel,
  historyViewModel: HistoryViewModel,
) {
  val gameId by historyViewModel.currentGameId.collectAsState(null)
  val game by remember(gameId) {
    gameId?.let { persistViewModel.getGame(it) } ?: flowOf(null)
  }.collectAsState(null)

  val pointer by historyViewModel.pointer.collectAsState(0)

  LaunchedEffect(game) {
    game?.moves?.let {
      historyViewModel.pointerLimit(it.size)
    }
  }

  val chessboardState = remember(game, pointer) {
    Log.e("POINTER", pointer.toString())
    var _board = Board.new()
    game?.let {
      val moves = it.moves.sortedBy { it.order }.slice(0 until  pointer)
      moves
        .map {
          val from = it.from.asSquare
          val to = it.to.asSquare
          when {
            from is Square && to is Square ->
              (from to to).squarePairAsMove
            else ->
              null
          }
        }
        .filterNotNull()
        .forEach {
          val nextBoard = _board.makeMove(it)
          _board = nextBoard ?: _board
        }
    }
    _board.pieces.map { piece ->
      val playerColor = game?.game?.playerColor
      val square = if(playerColor == PieceColor.BLACK.name)
        piece.square.value
      else piece.square.inverse.value
      val squareState = when {
        piece.type == PieceType.MAN && piece.color == PieceColor.BLACK ->
          SquareState.BLACK_MAN
        piece.type == PieceType.MAN && piece.color == PieceColor.RED ->
          SquareState.RED_MAN
        piece.type == PieceType.KING && piece.color == PieceColor.BLACK ->
          SquareState.BLACK_KING
        else ->
          SquareState.RED_KING
      }
      (square to squareState)
    }
  }

  val chessboardHighlight = remember(game, pointer) {
    game?.moves?.let {
      if (pointer <= 0) {
        return@remember listOf<Int>()
      }
      val lastMove = it[pointer - 1]
      if (game?.game?.playerColor == PieceColor.BLACK.name)
        listOf(
          lastMove.from,
          lastMove.to,
        )
      else
        listOf(
          lastMove.from.asSquare!!.inverse.value,
          lastMove.to.asSquare!!.inverse.value,
        )
    } ?: listOf<Int>()
  }

  Column(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
      ) {
        Text(
          text = game?.game?.enemy?.nickname ?: "Loading...",
          style = MaterialTheme.typography.bodyLarge,
          textAlign = TextAlign.End,
          modifier = Modifier.padding(end = 10.dp),
        )
        Icon(
          Icons.Default.AccountCircle,
          contentDescription = "Enemy Icon",
        )
      }
      Chessboard(
        state = chessboardState,
        highlight = chessboardHighlight,
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          IconButton(onClick = {historyViewModel.pointerPrev()}) {
            Icon(
              Icons.AutoMirrored.Filled.KeyboardArrowLeft,
              contentDescription = "Previous",
            )
          }
          IconButton(onClick = {historyViewModel.pointerRestart()}) {
            Icon(
              Icons.Default.Refresh,
              contentDescription = "Restart"
            )
          }
          IconButton(onClick = {historyViewModel.pointerNext(game?.moves?.size)}) {
            Icon(
              Icons.AutoMirrored.Filled.KeyboardArrowRight,
              contentDescription = "Next"
            )
          }
        }
      }
    }
  }
}