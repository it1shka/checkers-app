package com.it1shka.checkers

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
  private val NICKNAME_KEY = stringPreferencesKey("nickname")
  private val RATING_KEY = intPreferencesKey("rating")
  private val REGION_KEY = stringPreferencesKey("region")

  private fun <T> saveItem(key: Preferences.Key<T>): suspend (ctx: Context, value: T) -> Unit {
    return { ctx, value ->
      ctx.dataStore.edit { storage ->
        storage[key] = value
      }
    }
  }

  private fun <T> getItem(key: Preferences.Key<T>): (ctx: Context) -> Flow<T> {
    return { ctx ->
      ctx.dataStore.data
        .map { storage -> storage[key] }
        .filterNotNull()
    }
  }

  val saveDifficulty = saveItem(DIFFICULTY_KEY)
  val getDifficulty = getItem(DIFFICULTY_KEY)

  val saveColor = saveItem(COLOR_KEY)
  val getColor = getItem(COLOR_KEY)

  val saveNickname = saveItem(NICKNAME_KEY)
  val getNickname = getItem(NICKNAME_KEY)

  val saveRating = saveItem(RATING_KEY)
  val getRating = getItem(RATING_KEY)
  suspend fun incrementRating(ctx: Context) {
    ctx.dataStore.edit { storage ->
      val prev = storage[RATING_KEY]
      storage[RATING_KEY] = (prev ?: 0) + 1
    }
  }

  val saveRegion = saveItem(REGION_KEY)
  val getRegion = getItem(REGION_KEY)
}