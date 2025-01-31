package com.it1shka.checkers.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MoveEntity::class, GameEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun gameDao(): GameDao

  abstract fun moveDao(): MoveDao

  companion object {
    @Volatile
    private var Instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return Instance ?: synchronized(this) {
        Room.databaseBuilder(context, AppDatabase::class.java, "checkers_database")
          .allowMainThreadQueries()
          .build()
          .also { Instance = it }
      }
    }
  }
}