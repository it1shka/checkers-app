package com.it1shka.checkers.screens.online.records

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
sealed class IncomingMessage {

  @Serializable
  @SerialName("enemy")
  data class Enemy(
    @SerialName("payload")
    val payload: PlayerInfo,
  ) : IncomingMessage()

  @Serializable
  @SerialName("color")
  data class Color(
    @SerialName("payload")
    val payload: String,
  ) : IncomingMessage()

  @Serializable
  @SerialName("board")
  data class Board(
    @SerialName("payload")
    val payload: List<Piece>,
  ) : IncomingMessage()

  @Serializable
  @SerialName("status")
  data class Status(
    @SerialName("payload")
    val payload: String,
  ) : IncomingMessage()

  @Serializable
  @SerialName("time")
  data class Time(
    @SerialName("payload")
    val payload: Time,
  ) : IncomingMessage()
}