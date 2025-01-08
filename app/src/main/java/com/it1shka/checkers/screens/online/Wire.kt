package com.it1shka.checkers.screens.online

import android.util.Log
import com.it1shka.checkers.BuildConfig
import com.it1shka.checkers.screens.online.records.PlayerInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

object Wire {
  private const val SOCKET_HOST = BuildConfig.SOCKET_HOST
  private const val SOCKET_PORT = BuildConfig.SOCKET_PORT
  private val client = OkHttpClient()
  private var socket: WebSocket? = null
  private var _socketUp = MutableStateFlow(false)
  val socketUp = _socketUp.asStateFlow()

  fun greet(player: PlayerInfo) {
    if (socket != null) {
      bye()
    }
    val url = HttpUrl.Builder()
      .host(SOCKET_HOST)
      .port(SOCKET_PORT)
      .addQueryParameter("nickname", player.nickname)
      .addQueryParameter("rating", player.rating.toString())
      .addQueryParameter("region", player.region)
      .build()
    val request = Request.Builder()
      .url(url)
      .build()
    socket = client.newWebSocket(request, object : WebSocketListener() {
      override fun onOpen(webSocket: WebSocket, response: Response) {
        _socketUp.value = true
      }

      override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        _socketUp.value = false
      }

      override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        val message = response?.message ?: "no message"
        Log.e("Websocket", "Failure occurred: $message")
      }

      override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        // super.onMessage(webSocket, bytes)
      }

      override fun onMessage(webSocket: WebSocket, text: String) {
        // super.onMessage(webSocket, text)
      }
    })
  }

  fun bye() {
    socket?.close(
      code = 1000,
      reason = "client closed the connection",
    )
    socket = null
  }
}