package com.it1shka.checkers.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Enemy (
  val nickname: String,
  val rating: Int? = null,
  val region: String? = null,
)

@Entity
data class GameEntity (
  @PrimaryKey
  @ColumnInfo(name = "id")
  val id: String,

  @Embedded(prefix = "enemy_")
  val enemy: Enemy,
)