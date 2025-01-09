package com.it1shka.checkers.screens.online.records

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoardPayload(
  @SerialName("turn") val turn: String,
  @SerialName("pieces") val pieces: List<Piece>
)