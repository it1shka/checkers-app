package com.it1shka.checkers.screens.profile

private val badges = mapOf(
  0 to "Newborn",
  10 to "Novice",
  25 to "Novice+",
  50 to "Medium",
  100 to "Medium+",
  150 to "Advanced",
  200 to "Advanced+",
  250 to "Master",
  300 to "Master+",
  500 to "Grandmaster",
  1000 to "Champion",
  2000 to "Absolute Champion",
  5000 to "Legendary Champion",
)

fun getBadge(score: Int): Pair<Int, String> {
  val sortedBadges = badges
    .toList()
    .sortedByDescending { it.first }
  for (pair in sortedBadges) {
    if (score >= pair.first) {
      return pair
    }
  }
  return sortedBadges.last()
}

fun getNextBadge(score: Int): Pair<Int, String>? {
  val sortedBadges = badges
    .toList()
    .sortedBy { it.first }
  for (pair in sortedBadges) {
    if (score < pair.first) {
      return pair
    }
  }
  return null
}