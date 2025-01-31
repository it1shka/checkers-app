package com.it1shka.checkers.data

import androidx.room.Embedded
import androidx.room.Relation

data class GameWithMoves (
  @Embedded
  val game: GameEntity,

  @Relation(
    parentColumn = "id",
    entityColumn = "game_id",
  )
  val moves: List<MoveEntity>
)