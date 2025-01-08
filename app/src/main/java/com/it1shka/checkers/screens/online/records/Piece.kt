package com.it1shka.checkers.screens.online.records

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Piece(
  @SerialName("color") val color: String,
  @SerialName("square") val square: Int,
  @SerialName("type") val type: String,
)