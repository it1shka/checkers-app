package com.it1shka.checkers.screens.profile

import androidx.compose.ui.graphics.Color

data class Badge(
  val ranking: Int,
  val color: Color,
  val name: String
)

private val badges = listOf(
  Badge(0, Color(0xff303030), "Newborn"),
  Badge(10, Color(0xff525252), "Novice"),
  Badge(25, Color(0xff789a9c), "Novice+"),
  Badge(50, Color(0xff55cacf), "Medium"),
  Badge(100, Color(0xff2b67cf), "Medium+"),
  Badge(150, Color(0xff782bcf), "Advanced"),
  Badge(200, Color(0xff982bcf), "Advanced+"),
  Badge(250, Color(0xffcf852b), "Master"),
  Badge(300, Color(0xffcf572b), "Master+"),
  Badge(500, Color(0xffcf2b2b), "Grandmaster"),
  Badge(1000, Color(0xff2bcf4f), "Champion"),
  Badge(2000, Color(0xff2bcf7a), "Absolute Champion"),
  Badge(5000, Color(0xffcf557b), "Legend")
)

fun getCurrentBadge(score: Int): Badge {
  for (badge in badges.sortedByDescending { it.ranking }) {
    if (score >= badge.ranking) {
      return badge
    }
  }
  return badges.first()
}

fun getNextBadge(score: Int): Badge? {
  for (badge in badges.sortedBy { it.ranking }) {
    if (score < badge.ranking) {
      return badge
    }
  }
  return null
}

fun getCompletedBadges(score: Int): List<Badge> {
  return badges.filter { badge ->
    score >= badge.ranking
  }.sortedBy { it.ranking }
}

fun getFutureBadges(score: Int): List<Badge> {
  return badges.filter { badge ->
    score < badge.ranking
  }.sortedBy { it.ranking }
}