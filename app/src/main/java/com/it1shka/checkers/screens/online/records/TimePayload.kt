package com.it1shka.checkers.screens.online.records

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimePayload (
  @SerialName("player") val player: String,
  @SerialName("time") val time: Int,
)