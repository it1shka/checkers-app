package com.it1shka.checkers.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MoveDao {
  @Query("select * from moveentity where game_id = :gameId order by `order` asc")
  fun getMovesByGameId(gameId: Int): Flow<List<MoveEntity>>

  @Query("select count(*) from moveentity where game_id = :gameId")
  fun getMovesCountByGameId(gameId: Int): Flow<Int>

  @Insert
  suspend fun insertMove(move: MoveEntity)
}