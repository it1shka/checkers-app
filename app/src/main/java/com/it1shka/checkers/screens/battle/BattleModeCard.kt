package com.it1shka.checkers.screens.battle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable fun BattleModeCard(mode: BattleMode, onClick: () -> Unit) {
  OutlinedCard(
    modifier = Modifier.clickable(
      onClick = { onClick() }
    ),
    border = BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.outline,
    ),
  ) {
    Row (
      modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.surfaceContainer),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Icon(
        modifier = Modifier
          .height(60.dp)
          .width(60.dp)
          .padding(all = 10.dp),
        imageVector = mode.icon,
        contentDescription = mode.title,
      )
      Column (
        modifier = Modifier
          .fillMaxWidth()
          .padding(all = 10.dp)
      ) {
        Text(
          text = mode.title,
          style = MaterialTheme.typography.titleMedium,
        )
        Text(
          text = mode.subtitle,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
    }
  }
}