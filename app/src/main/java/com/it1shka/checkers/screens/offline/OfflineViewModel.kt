package com.it1shka.checkers.screens.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.it1shka.checkers.gamelogic.BoardMove
import com.it1shka.checkers.gamelogic.BotMinimax
import com.it1shka.checkers.gamelogic.BotRandom
import com.it1shka.checkers.gamelogic.CheckersBot
import com.it1shka.checkers.gamelogic.GameSession
import com.it1shka.checkers.gamelogic.GameStatus
import com.it1shka.checkers.gamelogic.MinimaxConfigs
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.gamelogic.Square
import com.it1shka.checkers.gamelogic.squarePairAsMove
import com.it1shka.checkers.screens.battle.BotDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OfflineState (
  val session: GameSession,
  val playerColor: PieceColor,
  val bot: CheckersBot,
  val pivotSquare: Square? = null
) {
  companion object {
    fun new(): OfflineState {
      return OfflineState(
        session = GameSession.new(),
        playerColor = PieceColor.BLACK,
        bot = BotMinimax(MinimaxConfigs.normal),
        pivotSquare = null
      )
    }
  }
}

class OfflineViewModel : ViewModel() {
  private val _state = MutableStateFlow(OfflineState.new())
  val state = _state.asStateFlow()

  private val _moves = Channel<BoardMove>(Channel.UNLIMITED)
  val moves = _moves.consumeAsFlow()

  private val _startRecording = Channel<String>(Channel.UNLIMITED)
  val startRecording = _startRecording.consumeAsFlow()

  fun setDifficulty(difficulty: BotDifficulty) {
    val newBot = when (difficulty) {
      BotDifficulty.EASY -> BotRandom()
      BotDifficulty.NORMAL -> BotMinimax(MinimaxConfigs.normal)
      BotDifficulty.HARD -> BotMinimax(MinimaxConfigs.intermediate)
      else -> BotMinimax(MinimaxConfigs.strong)
    }
    _state.update { offlineState ->
      offlineState.copy(bot = newBot)
    }
    _startRecording.trySend(newBot.name)
  }

  fun setPlayerColor(playerColor: PieceColor) {
    // TODO: here is the problem
    _startRecording.trySend(_state.value.bot.name)
    _state.update { offlineState ->
      offlineState.copy(
        session = GameSession.new(),
        playerColor = playerColor,
      )
    }
    if (playerColor == PieceColor.RED) {
      triggerBotResponseAsync()
    }
  }

  fun togglePlayerColor() {
    val currentColor = _state.value.playerColor
    val nextColor = PieceColor.opposite(currentColor)
    setPlayerColor(nextColor)
  }

  fun restart() {
    _startRecording.trySend(_state.value.bot.name)
    _state.update { offlineState ->
      offlineState.copy(
        session = GameSession.new(),
        pivotSquare = null,
      )
    }
    val playerColor = _state.value.playerColor
    if (playerColor == PieceColor.RED) {
      triggerBotResponseAsync()
    }
  }

  fun squareClick(clickedSquare: Square) {
    val state = _state.value
    val selfClick = state.session.pieceAt(clickedSquare)?.color == state.playerColor
    val myTurn = state.playerColor == state.session.turn
    val sessionActive = state.session.status == GameStatus.ACTIVE
    when {
      selfClick -> {
        val newPivotSquare = if (state.pivotSquare != clickedSquare)
          clickedSquare
          else null
        _state.update { offlineState ->
          offlineState.copy(
            pivotSquare = newPivotSquare,
          )
        }
      }

      !selfClick && state.pivotSquare != null && myTurn && sessionActive -> {
       (state.pivotSquare to clickedSquare).squarePairAsMove
         ?.let { move ->
           _moves.trySend(move)
           state.session.makeMove(move)
         }
         ?.let { nextSession ->
           _state.update { offlineState ->
             offlineState.copy(
               session = nextSession,
               pivotSquare = null,
             )
           }
           triggerBotResponseAsync()
         }
      }

      else -> {
        _state.update { offlineState ->
          offlineState.copy(
            pivotSquare = null,
          )
        }
      }
    }
  }

  private fun triggerBotResponseAsync() {
    viewModelScope.launch(Dispatchers.Default) {
      triggerBotResponse()
    }
  }

  private fun triggerBotResponse() {
    val bot = _state.value.bot
    val botColor = PieceColor.opposite(_state.value.playerColor)
    var session = _state.value.session
    while (session.status == GameStatus.ACTIVE && session.turn == botColor) {
      bot.findMove(session.board)
        ?.let { move ->
          session.makeMove(move)
        }
        ?.let { nextSession ->
          session = nextSession
          _state.update { offlineState ->
            offlineState.copy(
              session = nextSession,
            )
          }
        }
    }
  }

  override fun onCleared() {
    _moves.close()
    _startRecording.close()
  }
}