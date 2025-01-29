package com.it1shka.checkers.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun Clock(time: Int?) {
  val display = remember(time) {
    if (time == null) return@remember ""
    val minutes = (time / 60).toString().padStart(2, '0')
    val seconds = (time % 60).toString().padStart(2, '0')
    "$minutes:$seconds"
  }

  Text(
    text = display,
    style = MaterialTheme.typography.headlineSmall
  )
}