package com.it1shka.checkers.screens.history

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// This ViewModel should be
// injected from the outside
// (from AppNavigation)

class HistoryViewModel : ViewModel() {
  private val _currentGameId = MutableStateFlow<String?>(null)
  val currentGameId = _currentGameId.asStateFlow()

  private val _pointer = MutableStateFlow<Int>(0)
  val pointer = _pointer.asStateFlow()

  fun chooseGameId(newGameId: String) {
    _currentGameId.value = newGameId
    _pointer.value = 0
  }

  fun pointerPrev() {
    _pointer.update { (it - 1).coerceAtLeast(0) }
  }

  fun pointerNext(limit: Int? = null) {
    _pointer.update { prev ->
      limit?.let { limit -> (prev + 1).coerceAtMost(limit) } ?: (prev + 1)
    }
  }

  fun pointerLimit(limit: Int) {
    _pointer.update {
      it
        .coerceAtLeast(0)
        .coerceAtMost(limit)
    }
  }

  fun pointerRestart() {
    _pointer.value = 0
  }
}