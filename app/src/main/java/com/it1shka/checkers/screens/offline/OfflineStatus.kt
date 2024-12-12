package com.it1shka.checkers.screens.offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OfflineStatus(title: String, subtitle: String) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.Bottom,
  ) {
    Column (
      modifier = Modifier
        .fillMaxWidth()
        .padding(all = 16.dp),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.displaySmall,
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyLarge,
      )
    }
  }
}