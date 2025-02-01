package com.it1shka.checkers.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PersistViewModel(private val database: AppDatabase) : ViewModel() {
  private val game = MutableStateFlow<GameEntity?>(null)
  private val created = MutableStateFlow(false)
  private val moveOrder = MutableStateFlow(0)

  val games = database.gameDao().getGames()
  fun getGame(gameId: String) = database.gameDao().getGameWithMovesById(gameId)

  fun openGame(newGame: GameEntity) {
    moveOrder.value = 0
    game.value = newGame
    created.value = false
  }

  fun openGame(playerColor: String, enemy: Enemy) {
    openGame(GameEntity(
      id = UUID.randomUUID().toString(),
      playerColor = playerColor,
      playedAt = System.currentTimeMillis(),
      enemy = enemy,
    ))
  }

  fun addMove(from: Int, to: Int) {
    createGameIfNotExists()
    game.value?.let {
      val moveEntity = MoveEntity(
        id = UUID.randomUUID().toString(),
        gameId = it.id,
        order = moveOrder.value,
        from = from,
        to = to,
      )
      moveOrder.value += 1
      viewModelScope.launch(Dispatchers.IO) {
        database.moveDao().insertMove(moveEntity)
      }
    }
  }

  private fun createGameIfNotExists() {
    if (created.value) return
    game.value?.let {
      viewModelScope.launch(Dispatchers.IO) {
        database.gameDao().insertGame(it)
        created.value = true
      }
    }
  }
}

class PersistViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST")
    return PersistViewModel(database) as T
  }
}