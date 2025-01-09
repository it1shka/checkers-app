package com.it1shka.checkers.screens.online.records

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovePayload(
  @SerialName("from") val from: Int,
  @SerialName("to") val to: Int,
)