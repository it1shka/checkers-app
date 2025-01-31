package com.it1shka.checkers.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PersistViewModel(private val database: AppDatabase) : ViewModel() {
  private val gameId = MutableStateFlow<String?>(null)
  private val moveOrder = MutableStateFlow(0)

  fun openGame(nickname: String, rating: Int? = null, region: String? = null) {
    moveOrder.value = 0
    val enemy = Enemy(
      nickname = nickname,
      rating = rating,
      region = region,
    )
    val uuid = UUID.randomUUID().toString();
    val gameEntity = GameEntity(
      id = uuid,
      enemy = enemy,
    )
    viewModelScope.launch(Dispatchers.IO) {
      database.gameDao().insertGame(gameEntity)
    }
    gameId.value = uuid
  }

  fun addMove(from: Int, to: Int) {
    val currentGameId = gameId.value
    if (currentGameId == null) return
    val uuid = UUID.randomUUID().toString();
    val moveEntity = MoveEntity(
      id = uuid,
      gameId = currentGameId,
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

class PersistViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST")
    return PersistViewModel(database) as T
  }
}