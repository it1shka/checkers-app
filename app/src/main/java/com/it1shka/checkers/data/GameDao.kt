package com.it1shka.checkers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
  @Query("select * from gameentity order by playedAt desc")
  fun getGames(): Flow<List<GameEntity>>

  @Transaction
  @Query("select * from gameentity where id = :gameId")
  fun getGameWithMovesById(gameId: Int): Flow<List<GameWithMoves>>

  @Insert
  suspend fun insertGame(game: GameEntity)
}