package com.it1shka.checkers.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoveEntity (
  @PrimaryKey
  @ColumnInfo(name = "id")
  val id: String,

  @ColumnInfo(name = "game_id")
  val gameId: String,

  @ColumnInfo(name = "order")
  val order: Int,

  @ColumnInfo(name = "from")
  val from: Int,

  @ColumnInfo(name = "to")
  val to: Int,
)