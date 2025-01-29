package com.it1shka.checkers.screens.online.records

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class OutcomingMessage(@SerialName("type") val type: String) {
  @Serializable
  class Join : OutcomingMessage("join")

  @Serializable
  class Leave : OutcomingMessage("leave")

  @Serializable
  class Move(
    @SerialName("payload")
    val payload: MovePayload
  ) : OutcomingMessage("move")
}