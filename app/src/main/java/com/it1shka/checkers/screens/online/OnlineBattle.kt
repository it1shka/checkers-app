package com.it1shka.checkers.screens.online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.it1shka.checkers.components.Chessboard
import com.it1shka.checkers.components.Clock
import com.it1shka.checkers.components.SquareState
import com.it1shka.checkers.gamelogic.GameStatus
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.gamelogic.asSquare
import com.it1shka.checkers.screens.online.records.PlayerInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

private const val LOADING = "Loading..."

@Composable
fun OnlineBattle(
  player: PlayerInfo,
  enemy: PlayerInfo?,
  playerColor: PieceColor?,
  playerTime: Int?,
  enemyTime: Int?,
  gameStatus: GameStatus?,
  boardState: List<Pair<Int, SquareState>>?,
  turn: PieceColor?,
  onMove: (from: Int, to: Int) -> Unit,
  onBattleLeave: () -> Unit,
  onPlayAgain: () -> Unit,
) {
  val pieces = remember(playerColor, boardState) {
    if (playerColor == PieceColor.BLACK) return@remember boardState
    boardState?.map { piece ->
      val (rawSquare, squareState) = piece
      val inverseSquare = rawSquare.asSquare?.inverse?.value
      if (inverseSquare == null) null
      else inverseSquare to squareState
    }?.filterNotNull()
  }

  var chosenSquare by remember { mutableStateOf<Int?>(null) }

  fun squareTouch(rawSquare: Int) {
    val touchedSquare =
      if (playerColor == PieceColor.BLACK) rawSquare
      else {
        rawSquare.asSquare?.inverse?.value
      }

    if (touchedSquare == null) return

    when (chosenSquare) {
      null -> {
        chosenSquare = touchedSquare
      }
      touchedSquare -> {
        chosenSquare = null
      }
      else -> {
        chosenSquare?.let { from ->
          onMove(from, touchedSquare)
        }
        chosenSquare = null
      }
    }
  }

  // TODO: implement proper highlighting
  val highlight = remember(chosenSquare) {
    val square = if (playerColor == PieceColor.BLACK)
      chosenSquare
      else chosenSquare?.asSquare?.inverse?.value
    square?.let { listOf(it) } ?: listOf()
  }

  TextButton(onClick = { onBattleLeave() }) {
    Icon(
      Icons.AutoMirrored.Default.KeyboardArrowLeft,
      contentDescription = "Leave battle",
    )
    Text("Leave")
  }

  Column(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
  ) {
    Column(
      verticalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier
        .fillMaxWidth()
    ) {
      // enemy information
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Icon(
            Icons.Default.AccountCircle,
            contentDescription = "Bot Icon",
          )
          Column {
            val nickname = enemy?.nickname ?: LOADING
            Text(
              text = nickname,
              style = MaterialTheme.typography.bodyLarge,
            )
            val region = enemy?.region ?: LOADING
            val wins = enemy?.rating?.toString() ?: LOADING
            Text(
              text = "From $region, $wins wins",
              style = MaterialTheme.typography.bodySmall,
            )
          }
        }
        Clock(enemyTime)
      }

      // chessboard
      Chessboard(
        state = pieces ?: listOf(),
        highlight = highlight,
        onSquareClick = ::squareTouch
      )

      // player information
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Clock(playerTime)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
          Column {
            Text(
              text = player.nickname,
              style = MaterialTheme.typography.bodyLarge,
            )
            Text(
              text = "From ${player.region}, ${player.rating} wins",
              style = MaterialTheme.typography.bodySmall,
            )
          }
          Icon(
            Icons.Default.AccountCircle,
            contentDescription = "Player Icon",
          )
        }
      }

      // player turn
      if (turn != null && gameStatus == GameStatus.ACTIVE) {
        val turnLabel =
          if (turn == playerColor) "Your turn"
          else "Opponent's turn"
        Text(
          text = turnLabel,
          style = MaterialTheme.typography.headlineSmall,
          color = MaterialTheme.colorScheme.secondary,
        )
      }
    }
  }

  // displaying the status
  if (gameStatus != null && gameStatus != GameStatus.ACTIVE) {
    val winner = gameStatus.winnerColor
    val label = when {
      winner != null && playerColor == winner ->
        "You won!"
      winner != null && playerColor != winner ->
        "You lost!"
      else ->
        "Draw"
    }
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0x4d000000)),
      verticalArrangement = Arrangement.Center,
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(color = MaterialTheme.colorScheme.background)
          .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
      ) {
        Text(
          text = label,
          style = MaterialTheme.typography.headlineMedium,
        )
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Button(onClick = { onPlayAgain() }) {
            Text("Again!")
          }
          TextButton(onClick = { onBattleLeave() }) {
            Text("Menu")
          }
        }
      }
    }
  }
}