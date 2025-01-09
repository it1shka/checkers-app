package com.it1shka.checkers.screens.online

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.it1shka.checkers.components.Chessboard
import com.it1shka.checkers.components.Clock
import com.it1shka.checkers.components.SquareState
import com.it1shka.checkers.gamelogic.GameStatus
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.gamelogic.asSquare
import com.it1shka.checkers.screens.online.records.PlayerInfo

private const val LOADING = "Loading..."

@Composable
fun OnlineBattle(
  nav: NavController,
  player: PlayerInfo,
  enemy: PlayerInfo?,
  playerColor: PieceColor?,
  playerTime: Int?,
  enemyTime: Int?,
  gameStatus: GameStatus?,
  boardState: List<Pair<Int, SquareState>>?,
  turn: PieceColor?,
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
        // TODO:
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
    }
  }
}