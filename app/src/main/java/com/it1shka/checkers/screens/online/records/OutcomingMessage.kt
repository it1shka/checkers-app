package com.it1shka.checkers.screens.online.records

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class OutcomingMessage(
  @SerialName("type") val type: String,
) {

  @Serializable
  class Join : OutcomingMessage("join")

  @Serializable
  class Leave : OutcomingMessage("leave")

  @Serializable
  data class Move(
    @SerialName("from") val from: Int,
    @SerialName("to") val to: Int,
  ) : OutcomingMessage("move")

}