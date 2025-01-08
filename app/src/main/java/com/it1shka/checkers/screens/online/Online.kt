package com.it1shka.checkers.screens.online

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.it1shka.checkers.components.SquareState
import com.it1shka.checkers.gamelogic.GameStatus
import com.it1shka.checkers.screens.online.records.IncomingMessage
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import com.it1shka.checkers.components.ConfirmBackHandler

@Composable
fun Online(
  nickname: String,
  rating: Int,
  region: String,
  socketViewModel: SocketViewModel = viewModel(),
  mainViewModel: OnlineViewModel = viewModel(),
) {
  val coroutineScope = rememberCoroutineScope()
  LaunchedEffect(Unit) {
    coroutineScope.launch {
      socketViewModel.incomingMessage.collect { message ->
        when (message) {
          is IncomingMessage.QueueJoined -> mainViewModel.joinQueue()
          is IncomingMessage.QueueLeft -> mainViewModel.leaveQueue()
          is IncomingMessage.Enemy -> mainViewModel.joinBattle(message.payload)
          is IncomingMessage.Color -> mainViewModel.setPlayerColor(message.payload.asColor)
          is IncomingMessage.Time -> mainViewModel.setTime(
            message.payload.player.asColor,
            message.payload.time
          )
          is IncomingMessage.Status -> {
            val status = when (message.payload) {
              "active" -> GameStatus.ACTIVE
              // TODO: implement generic draw
              // TODO: here DRAW_REPEAT_BOARD == DRAW_WITHOUT_CAPTURE
              "draw" -> GameStatus.DRAW_REPEAT_BOARD
              "red" -> GameStatus.RED_WON
              "black" -> GameStatus.BLACK_WON
              else -> null
            }
            if (status == null) {
              Log.e("Online", "unknown status message: ${message.payload}")
            } else {
              mainViewModel.setGameStatus(status)
            }
          }
          is IncomingMessage.Board -> {
            val pieces = message.payload.map { jsonPiece ->
              val squareState = when {
                jsonPiece.color == "black" && jsonPiece.type == "man" ->
                  SquareState.BLACK_MAN
                jsonPiece.color == "black" && jsonPiece.type == "king" ->
                  SquareState.BLACK_KING
                jsonPiece.color == "red" && jsonPiece.type == "man" ->
                  SquareState.RED_MAN
                jsonPiece.color == "red" && jsonPiece.type == "king" ->
                  SquareState.RED_KING
                else -> null
              }
              if (squareState == null) {
                Log.e("Online", "failed to convert color: ${jsonPiece.color} + type: ${jsonPiece.type} to a square state")
                null
              } else {
                jsonPiece.square to squareState
              }
            }.filterNotNull()
            mainViewModel.setBoardState(pieces)
          }

          else -> {
            Log.e("Online Battle", "unexpected message")
          }
        }
      }
    }
  }

  val socketState by socketViewModel.socketState.collectAsState()
  val screenState by mainViewModel.state.collectAsState()

  when (screenState) {
    OnlineState.IN_MENU -> {
      // TODO:
    }
    OnlineState.IN_QUEUE -> {
      ConfirmBackHandler(
        title = "Quitting battle queue",
        text = "You will stop searching for an enemy"
      ) {
        // TODO:
      }
      // TODO:
    }
    OnlineState.IN_BATTLE -> {
      ConfirmBackHandler(
        title = "Quitting battle",
        text = "Are you sure? You will lose this battle",
      ) {
        // TODO:
      }
      // TODO:
    }
  }
}