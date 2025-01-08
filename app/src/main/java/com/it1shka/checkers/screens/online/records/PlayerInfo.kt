package com.it1shka.checkers.screens.online.records

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerInfo (
  @SerialName("nickname") val nickname: String,
  @SerialName("rating") val rating: Int,
  @SerialName("region") val region: String,
)