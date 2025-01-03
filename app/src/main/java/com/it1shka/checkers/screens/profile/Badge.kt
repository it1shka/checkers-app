package com.it1shka.checkers.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.round

@Composable
fun Badge(score: Int) {
  val (completedGoal, currentBadge) = remember(score) {
    getBadge(score)
  }
  val (nextGoal, nextBadge) = remember(score) {
    getNextBadge(score) ?: (null to null)
  }

  val progress = remember(score, completedGoal, nextGoal) {
    if (nextGoal == null) return@remember 1.0f
    val totalDelta = nextGoal - completedGoal
    val delta = score - completedGoal
    delta.toFloat() / totalDelta.toFloat()
  }

  val prettyProgress = remember(progress) {
    round(progress * 100.0f * 100.0f) / 100.0f
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Row(
      horizontalArrangement = Arrangement.Center,
    ) {
      Text(
        text = currentBadge,
        style = MaterialTheme.typography.headlineSmall,
      )
      // TODO: add icon
    }
    Box(
      modifier = Modifier
        .fillMaxWidth(0.85f)
        .background(Color(0xccccccff))
        .height(32.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxHeight()
          .fillMaxWidth(progress)
          .background(Color(0x66fc03ff))
      ) {}
    }
    val nextBadgeText =
      if (nextBadge != null) "$prettyProgress% to $nextBadge"
      else "Max badge reached!"
    Text(
      text = nextBadgeText,
      style = MaterialTheme.typography.labelMedium,
    )

    if (nextBadge != null) {
      Text(
        modifier = Modifier.padding(vertical = 16.dp),
        text = "Play in battles to earn badges",
        style = MaterialTheme.typography.labelSmall,
      )
    }
  }
}