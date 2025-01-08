package com.it1shka.checkers.screens.online

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BattleMenu(
  nickname: String,
  rating: Int,
  region: String,
  connected: Boolean,
) {
  Column(
    modifier = Modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
  ) {
    if (!connected) {
      Text(
        text = "You are offline",
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
      )
    } else {

    }
  }
}