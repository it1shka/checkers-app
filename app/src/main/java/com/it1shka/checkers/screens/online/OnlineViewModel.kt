package com.it1shka.checkers.screens.online

import androidx.lifecycle.ViewModel
import com.it1shka.checkers.components.SquareState
import com.it1shka.checkers.gamelogic.GameStatus
import com.it1shka.checkers.gamelogic.PieceColor
import com.it1shka.checkers.screens.online.records.PlayerInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class OnlineState {
  IN_MENU,
  IN_QUEUE,
  IN_BATTLE,
}

class OnlineViewModel : ViewModel() {
  private val _state = MutableStateFlow(OnlineState.IN_MENU)
  val state = _state.asStateFlow()

  private val _enemy = MutableStateFlow<PlayerInfo?>(null)
  val enemy = _enemy.asStateFlow()

  private val _color = MutableStateFlow<PieceColor?>(null)
  val color = _color.asStateFlow()

  private val _playerTime = MutableStateFlow<Int?>(null)
  val playerTime = _playerTime.asStateFlow()

  private val _enemyTime = MutableStateFlow<Int?>(null)
  val enemyTime = _enemyTime.asStateFlow()

  private val _gameStatus = MutableStateFlow<GameStatus?>(null)
  val gameStatus = _gameStatus.asStateFlow()

  private val _boardState = MutableStateFlow<List<Pair<Int, SquareState>>?>(null)
  val boardState = _boardState.asStateFlow()

  private val _turn = MutableStateFlow<PieceColor?>(null)
  val turn = _turn.asStateFlow()

  fun joinQueue() {
    _state.value = OnlineState.IN_QUEUE
  }

  fun leaveQueue() {
    _state.value = OnlineState.IN_MENU
  }

  fun joinBattle(enemy: PlayerInfo) {
    _state.value = OnlineState.IN_BATTLE
    _enemy.value = enemy
  }

  fun setPlayerColor(color: PieceColor) {
    _color.value = color
  }

  fun leaveBattle() {
    _state.value = OnlineState.IN_MENU
    _enemy.value = null
    _color.value = null
    _playerTime.value = null
    _enemyTime.value = null
    _gameStatus.value = null
    _boardState.value = null
  }

  fun setTime(player: PieceColor, time: Int) {
    if (_color.value == null) return
    if (_color.value == player) {
      _playerTime.value = time
    } else {
      _enemyTime.value = time
    }
  }

  fun setGameStatus(status: GameStatus) {
    _gameStatus.value = status
  }

  fun setBoardState(pieces: List<Pair<Int, SquareState>>, turn: PieceColor) {
    _boardState.value = pieces
    _turn.value = turn
  }
}