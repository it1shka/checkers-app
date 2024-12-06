package com.it1shka.checkers.screens.offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.it1shka.checkers.components.Chessboard
import com.it1shka.checkers.components.ConfirmBackHandler
import com.it1shka.checkers.components.SquareState
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.gamelogic.PieceType
import com.it1shka.checkers.gamelogic.asSquare

@Composable
fun Offline(nav: NavController, viewModel: OfflineViewModel = viewModel()) {
  val state by viewModel.state.collectAsState()

  val chessboardState = remember(state) {
    state.session.pieces.map { piece ->
      val square = piece.square.value
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

  val chessboardHighlight = remember(state) {
    val pivot = state.pivotSquare
    if (pivot == null) return@remember listOf()
    state.session
      .possibleMovesAt(pivot)
      .map { move -> move.to }
      .plus(pivot)
      .map { it.value }
  }

  fun squareClick(raw: Int) {
    raw.asSquare?.let { square ->
      viewModel.squareClick(square)
    }
  }

  ConfirmBackHandler(
    title = "Are you sure?",
    text = "You will lose this battle"
  ) {
    nav.popBackStack()
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
      Chessboard(
        state = chessboardState,
        highlight = chessboardHighlight,
        onSquareClick = { squareClick(it) }
      )
    }
  }
}