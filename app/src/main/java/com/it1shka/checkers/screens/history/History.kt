package com.it1shka.checkers.screens.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.it1shka.checkers.app.AppScreen
import com.it1shka.checkers.data.PersistViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private fun Long.asPrettyTimestamp(): String {
  val instant = Instant.ofEpochMilli(this)
  val datetime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
  val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
  return datetime.format(formatter)
}

@Composable
fun History(
  nav: NavController,
  persistViewModel: PersistViewModel,
  historyViewModel: HistoryViewModel,
) {
  val games by persistViewModel.games.collectAsState(listOf())

  fun chooseGame(gameId: String) {
    historyViewModel.chooseGameId(gameId)
    nav.navigate(AppScreen.HISTORY_REPLAY.name)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp)
  ) {
    if (games.isEmpty()) {
      Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Icon(
          Icons.Default.Warning,
          contentDescription = "No battles"
        )
        Text(
          modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
          textAlign = TextAlign.Center,
          text = "No battles played",
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.secondary
        )
      }
    }

    LazyColumn {
      items(games.size) { gameIdx ->
        val game = games[gameIdx]
        OutlinedCard(
          border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
          ),
          modifier = Modifier
            .padding(top = 8.dp)
            .clickable(
              enabled = true,
              onClick = { chooseGame(game.id) }
            )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(color = MaterialTheme.colorScheme.surfaceContainer),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp)
            ) {
              Text(
                text = "Battle with ${game.enemy.nickname}",
                style = MaterialTheme.typography.titleMedium,
              )
              val stamp = game.playedAt.asPrettyTimestamp()
              Text(
                text = "Played at: $stamp",
                style = MaterialTheme.typography.bodyMedium,
              )
            }
          }
        }
      }
    }
  }
}