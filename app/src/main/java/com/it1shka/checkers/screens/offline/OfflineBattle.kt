package com.it1shka.checkers.screens.offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.it1shka.checkers.components.ConfirmBackHandler

@Composable fun OfflineBattle(navController: NavController) {
  ConfirmBackHandler(
    navController = navController,
    title = "Quit the battle?",
    text = "You will lose this match",
  ) {

  }

  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize(),
  ) {
    Text("Offline Battle")
  }
}