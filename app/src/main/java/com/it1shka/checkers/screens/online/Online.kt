package com.it1shka.checkers.screens.online

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.it1shka.checkers.components.SquareState
import com.it1shka.checkers.gamelogic.GameStatus
import com.it1shka.checkers.screens.online.records.IncomingMessage
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.it1shka.checkers.components.ConfirmBackHandler
import com.it1shka.checkers.components.withConfirmation
import com.it1shka.checkers.screens.online.records.MovePayload
import com.it1shka.checkers.screens.online.records.OutcomingMessage
import com.it1shka.checkers.screens.online.records.PlayerInfo

@Composable
fun Online(
  nav: NavController,
  nickname: String,
  rating: Int,
  region: String,
  socketViewModel: SocketViewModel = viewModel(),
  mainViewModel: OnlineViewModel = viewModel(),
) {
  val player = remember(nickname, rating, region) {
    PlayerInfo(
      nickname = nickname,
      rating = rating,
      region = region,
    )
  }

  fun connectWebsocket() {
    socketViewModel.initSocket(player)
  }

  DisposableEffect(nickname, rating, region) {
    connectWebsocket()
    onDispose {
      socketViewModel.closeSocket()
      mainViewModel.leaveBattle()
      mainViewModel.leaveQueue()
    }
  }

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
            val pieces = message.payload.pieces.map { jsonPiece ->
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
            mainViewModel.setBoardState(pieces, message.payload.turn.asColor)
          }

          else -> {
            Log.e("Online Battle", "unexpected message")
          }
        }
      }
    }
  }

  fun joinQueue() {
    socketViewModel.sendMessage(
      OutcomingMessage.Join()
    )
  }

  fun move(from: Int, to: Int) {
    socketViewModel.sendMessage(
      OutcomingMessage.Move(
        payload = MovePayload(
          from = from,
          to = to,
        )
      )
    )
  }

  fun leaveBattle() {
    socketViewModel.sendMessage(
      OutcomingMessage.Leave()
    )
    mainViewModel.leaveBattle()
  }

  val socketState by socketViewModel.socketState.collectAsState()
  LaunchedEffect(socketState) {
    if (socketState == SocketState.CLOSED) {
      Log.e("Online", "Quitting due to the network error")
      mainViewModel.leaveBattle()
      mainViewModel.leaveQueue()
    }
  }

  val screenState by mainViewModel.state.collectAsState()

  val enemy by mainViewModel.enemy.collectAsState()
  val playerColor by mainViewModel.color.collectAsState()
  val playerTime by mainViewModel.playerTime.collectAsState()
  val enemyTime by mainViewModel.enemyTime.collectAsState()
  val gameStatus by mainViewModel.gameStatus.collectAsState()
  val boardState by mainViewModel.boardState.collectAsState()
  val turn by mainViewModel.turn.collectAsState()

  val context = LocalContext.current
  fun leaveBattleWithConfirmation() {
    if (gameStatus == null || gameStatus == GameStatus.ACTIVE) {
      withConfirmation(
        context = context,
        title = "Quitting battle",
        message = "Are you sure? You will lose this battle"
      ) {
        leaveBattle()
      }
      return
    }
    leaveBattle()
  }

  when (screenState) {
    OnlineState.IN_MENU -> {
      BattleMenu(
        nav = nav,
        nickname = nickname,
        rating = rating,
        region = region,
        connected = socketState == SocketState.OPEN,
        onReconnect = ::connectWebsocket,
        onQueueJoin = ::joinQueue,
      )
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
      if (gameStatus == null || gameStatus == GameStatus.ACTIVE) {
        ConfirmBackHandler(
          title = "Quitting battle",
          text = "Are you sure? You will lose this battle",
        ) {
          leaveBattle()
        }
      }
      OnlineBattle(
        player = player,
        enemy = enemy,
        playerColor = playerColor,
        playerTime = playerTime,
        enemyTime = enemyTime,
        gameStatus = gameStatus,
        boardState = boardState,
        turn = turn,
        onMove = ::move,
        onBattleLeave = ::leaveBattleWithConfirmation,
        onPlayAgain = ::joinQueue,
      )
    }
  }
}