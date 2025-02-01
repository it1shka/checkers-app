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
  private val enemy = MutableStateFlow<Enemy?>(null)
  private val startTime = MutableStateFlow<Long>(System.currentTimeMillis())
  private val moveOrder = MutableStateFlow(0)

  val games = database.gameDao().getGames()
  fun gameMoveCount(gameId: String) = database.gameDao().getGameWithMovesById(gameId)

  fun openGame(nickname: String, rating: Int? = null, region: String? = null) {
    moveOrder.value = 0
    gameId.value = null
    enemy.value = Enemy(
      nickname = nickname,
      rating = rating,
      region = region,
    )
    startTime.value = System.currentTimeMillis()
  }

  fun addMove(from: Int, to: Int) {
    createGameIfNotExists()
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

  private fun createGameIfNotExists() {
    if (gameId.value != null) return
    enemy.value?.let { currentEnemy ->
      val uuid = UUID.randomUUID().toString();
      val gameEntity = GameEntity(
        id = uuid,
        enemy = currentEnemy,
        playedAt = startTime.value,
      )
      viewModelScope.launch(Dispatchers.IO) {
        database.gameDao().insertGame(gameEntity)
      }
      gameId.value = uuid
    }
  }
}

class PersistViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST")
    return PersistViewModel(database) as T
  }
}