package com.it1shka.checkers.screens.offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.it1shka.checkers.app.AppScreen
import com.it1shka.checkers.components.Chessboard
import com.it1shka.checkers.components.ConfirmBackHandler

@Composable
fun Offline(navigateTo: (AppScreen) -> Unit) {
  ConfirmBackHandler(
    title = "Are you sure?",
    text = "You will lose this battle"
  ) {
    navigateTo(AppScreen.BATTLE)
  }

  Column(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ){
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
      Text(
        text = "1:11", // TODO:
        style = MaterialTheme.typography.displaySmall,
      )
      // TODO:
      Chessboard(

      )
      Text(
        text = "Your turn", // TODO:
        style = MaterialTheme.typography.displaySmall,
      )
    }
  }
}