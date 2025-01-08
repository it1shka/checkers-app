package com.it1shka.checkers.screens.online

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.it1shka.checkers.Preferences
import kotlinx.coroutines.launch

// TODO:

@Composable
fun Online(
  viewModel: SocketViewModel = viewModel(),
) {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(Unit) {
    coroutineScope.launch {
      Preferences.incrementRating(context)
    }
  }

  Column {
    // TODO:
  }
}