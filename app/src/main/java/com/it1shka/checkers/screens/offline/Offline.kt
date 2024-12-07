package com.it1shka.checkers.screens.offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.it1shka.checkers.Preferences
import com.it1shka.checkers.components.Chessboard
import com.it1shka.checkers.components.ConfirmBackHandler
import com.it1shka.checkers.components.SquareState
import com.it1shka.checkers.components.withConfirmation
import com.it1shka.checkers.gamelogic.GameStatus
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.gamelogic.PieceType
import com.it1shka.checkers.gamelogic.asSquare
import com.it1shka.checkers.screens.battle.BotDifficulty

@Composable
fun Offline(nav: NavController, viewModel: OfflineViewModel = viewModel()) {
  val context = LocalContext.current

  val state by viewModel.state.collectAsState()

  val botDifficulty by Preferences
    .getDifficulty(context)
    .collectAsState(BotDifficulty.NORMAL.name)

  LaunchedEffect(botDifficulty) {
    val difficulty = BotDifficulty.valueOf(botDifficulty)
    viewModel.setDifficulty(difficulty)
  }

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

  val turnText = remember(state) {
    if (state.session.turn == state.playerColor)
      "Your turn"
      else "Bot's turn"
  }

  val statusText = remember(state) {
    when (state.session.status) {
      GameStatus.RED_WON ->
        if (state.playerColor == PieceColor.RED)
          "You won"
          else "You lost"
      GameStatus.BLACK_WON ->
        if (state.playerColor == PieceColor.BLACK)
          "You won"
          else "You lost"
      GameStatus.DRAW_REPEAT_BOARD ->
        "Draw (position repeated 3 times)"
      GameStatus.DRAW_WITHOUT_CAPTURE ->
        "Draw (40 moves without capture)"
      else -> "Active"
    }
  }

  fun handleLeaveBattle() {
    withConfirmation(context, message = "You will lose this battle") {
      nav.popBackStack()
    }
  }

  fun handleRestartBattle() {
    withConfirmation(context, message = "You will lose your progress") {
      // TODO:
    }
  }

  fun handleColorChange() {
    withConfirmation(context, message = "The game will restart and the progress will be lost") {
      // TODO:
    }
  }

  ConfirmBackHandler(
    title = "Are you sure?",
    text = "You will lose this battle"
  ) {
    nav.popBackStack()
  }

  OfflineDropdownMenu(actions = MenuActions.empty().copy(
    onLeaveBattle = { handleLeaveBattle() },
    onRestart = { handleRestartBattle() },
    onColorChange = { handleColorChange() },
  ))

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
          text = state.bot.name,
          style = MaterialTheme.typography.bodyLarge,
          textAlign = TextAlign.End,
          modifier = Modifier.padding(end = 10.dp),
        )
        Icon(
          Icons.Default.AccountCircle,
          contentDescription = "Bot Icon",
        )
      }
      Chessboard(
        state = chessboardState,
        highlight = chessboardHighlight,
        onSquareClick = { squareClick(it) }
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = turnText,
          style = MaterialTheme.typography.bodyLarge,
        )
        Text(
          text = statusText,
          style = MaterialTheme.typography.bodyLarge,
        )
      }
    }
  }
}