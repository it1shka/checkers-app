package com.it1shka.checkers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "checkers_persistent_storage"

private val Context.dataStore by preferencesDataStore(
  name = DATASTORE_NAME
)

object Preferences {
  private val DIFFICULTY_KEY = stringPreferencesKey("difficulty")
  private val COLOR_KEY = stringPreferencesKey("color")

  suspend fun saveDifficulty(context: Context, difficulty: String) {
    context.dataStore.edit { storage ->
      storage[DIFFICULTY_KEY] = difficulty
    }
  }

  fun getDifficulty(context: Context): Flow<String> {
    return context.dataStore.data
      .map { storage -> storage[DIFFICULTY_KEY] }
      .filterNotNull()
  }

  suspend fun saveColor(context: Context, color: String) {
    context.dataStore.edit { storage ->
      storage[COLOR_KEY] = color
    }
  }

  fun getColor(context: Context): Flow<String> {
    return context.dataStore.data
      .map { storage -> storage[COLOR_KEY] }
      .filterNotNull()
  }
}