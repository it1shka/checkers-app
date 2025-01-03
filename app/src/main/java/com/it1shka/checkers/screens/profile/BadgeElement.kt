package com.it1shka.checkers.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.log2
import kotlin.math.round

@Composable
private fun BadgeStripe(badge: Badge) {
  val stripeWidth = remember(badge.ranking) {
    log2(badge.ranking.toFloat()) * 20
  }

  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
  ) {
    Box(
      modifier = Modifier
        .height(12.dp)
        .width(stripeWidth.dp)
        .background(color = badge.color)
    ) {}
    Text(
      text = badge.name,
      style = MaterialTheme.typography.bodySmall,
    )
  }
}

@Composable
fun BadgeElement(score: Int) {
  val currentBadge = remember(score) { getCurrentBadge(score) }
  val maybeNextBadge = remember(score) { getNextBadge(score) }
  val completedBadges = remember(score) { getCompletedBadges(score) }
  val nextBadges = remember(score) { getFutureBadges(score) }

  val progress = remember(score, currentBadge, maybeNextBadge) {
    if (maybeNextBadge == null) return@remember 1.0f
    val totalDelta = maybeNextBadge.ranking - currentBadge.ranking
    val delta = score - currentBadge.ranking
    delta.toFloat() / totalDelta.toFloat()
  }

  val prettyProgress = remember(progress) {
    round(progress * 100.0f * 100.0f) / 100.0f
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = currentBadge.name,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(horizontal = 8.dp),
      )
      Box(
        modifier = Modifier
          .width(16.dp)
          .height(16.dp)
          .background(color = currentBadge.color, shape = CircleShape)
          .aspectRatio(1f)
      ) {}
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
      if (maybeNextBadge != null) "$prettyProgress% to ${maybeNextBadge.name}"
      else "Max badge reached!"
    Text(
      text = nextBadgeText,
      style = MaterialTheme.typography.labelMedium,
    )

    if (maybeNextBadge != null) {
      Text(
        modifier = Modifier.padding(vertical = 16.dp),
        text = "Play in battles to earn badges",
        style = MaterialTheme.typography.labelSmall,
      )
    }

    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
      for (completedBadge in completedBadges) {
        BadgeStripe(completedBadge)
      }
      BadgeStripe(
        Badge(
          ranking = score,
          name = "<<< YOU",
          color = currentBadge.color,
        )
      )
      for (futureBadge in nextBadges) {
        BadgeStripe(futureBadge)
      }
    }
  }
}