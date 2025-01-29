package com.it1shka.checkers.screens.online

import android.util.Log
import androidx.lifecycle.ViewModel
import com.it1shka.checkers.BuildConfig
import com.it1shka.checkers.screens.online.records.IncomingMessage
import com.it1shka.checkers.screens.online.records.OutcomingMessage
import com.it1shka.checkers.screens.online.records.PlayerInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.nio.charset.StandardCharsets

enum class SocketState {
  CLOSED,
  OPEN,
}

val jsonReceive = Json {
  ignoreUnknownKeys = true
  classDiscriminator = "type"
}

@OptIn(ExperimentalSerializationApi::class)
val jsonSend = Json {
  classDiscriminatorMode = ClassDiscriminatorMode.NONE
}

class SocketViewModel : ViewModel() {
  private val SOCKET_SCHEME = BuildConfig.SOCKET_SCHEME
  private val SOCKET_HOST = BuildConfig.SOCKET_HOST
  private val SOCKET_PORT = BuildConfig.SOCKET_PORT

  private val client = OkHttpClient()
  private var socket: WebSocket? = null

  private val _socketState = MutableStateFlow(SocketState.CLOSED)
  val socketState = _socketState.asStateFlow()

  private val _incomingMessages = Channel<IncomingMessage>(Channel.UNLIMITED)
  val incomingMessage = _incomingMessages.consumeAsFlow()

  private fun getSocketRequest(player: PlayerInfo): Request {
    // https://github.com/square/okhttp/issues/4035
    val url = "$SOCKET_SCHEME://$SOCKET_HOST:$SOCKET_PORT/ws-connect?nickname=${player.nickname}&rating=${player.rating}&region=${player.region}"
    val request = Request.Builder()
      .url(url)
      .build()
    return request
  }

  fun initSocket(player: PlayerInfo) {
    try {
      if (socket != null) closeSocket()
      val request = getSocketRequest(player)
      socket = client.newWebSocket(request, object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) =
          onSocketOpen(webSocket, response)

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) =
          onSocketClosed(webSocket, code, reason)

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) =
          onSocketFailure(webSocket, t, response)

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) =
          onSocketMessage(webSocket, bytes)

        override fun onMessage(webSocket: WebSocket, text: String) =
          onSocketMessage(webSocket, text)
      })
    } catch (e: Exception) {
      Log.e("Websocket Failure", e.message ?: "no message")
      socket = null
      _socketState.value = SocketState.CLOSED
    }
  }

  private fun onSocketOpen(webSocket: WebSocket, response: Response) {
    _socketState.value = SocketState.OPEN
  }

  private fun onSocketClosed(webSocket: WebSocket, code: Int, reason: String) {
    _socketState.value = SocketState.CLOSED
  }

  private fun onSocketFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
    val message = response?.message ?: "no message"
    val failure = t.message ?: "no message"
    Log.e("WebSocket Failure", "Response: $message")
    Log.e("WebSocket Failure", "Failure: $failure")
    Log.e("Websocket Failure", "Closing websocket due to the failure")
    closeSocket()
  }

  private fun onSocketMessage(webSocket: WebSocket, bytes: ByteString) {
    webSocket.send("client does not support binary messages")
    Log.e("WebSocket Weird Message", bytes.string(StandardCharsets.UTF_8))
  }

  private fun onSocketMessage(webSocket: WebSocket, text: String) {
    try {
      jsonReceive
        .decodeFromString<IncomingMessage>(text)
        .let { msg ->
          _incomingMessages.trySend(msg)
        }
    } catch (e: SerializationException) {
      val message = e.message ?: "no message"
      Log.e("Websocket Malformed JSON", message)
    }
  }

  fun sendMessage(message: OutcomingMessage) {
    if (_socketState.value == SocketState.CLOSED) {
      return
    }
    socket?.let { ws ->
      try {
        val preparedMessage = jsonSend.encodeToString(message)
        ws.send(preparedMessage)
      } catch (e: Exception) {
        val message = e.message ?: "no message"
        Log.e("Websocket Send Failure", message)
      }
    }
  }

  fun closeSocket() {
    socket?.close(
      code = 1000,
      reason = "client closed the connection",
    )
    _socketState.value = SocketState.CLOSED
    socket = null
  }

  override fun onCleared() {
    super.onCleared()
    _incomingMessages.close()
    closeSocket()
  }
}