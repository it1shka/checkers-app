package com.it1shka.checkers.screens.offline

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val TIMER_DELAY = 1000L

class TimerViewModel : ViewModel() {
  private val _timer = MutableStateFlow(0)
  val timer = _timer.asStateFlow()
  private var timerJob = mutableStateOf<Job?>(null)

  fun startTimer() {
    if (timerJob.value != null) return
    timerJob.value = viewModelScope.launch(Dispatchers.IO) {
      _timer.update { 0 }
      while (true) {
        delay(TIMER_DELAY)
        _timer.update { it + 1 }
      }
    }
  }

  fun stopTimer() {
    timerJob.value?.cancel()
    timerJob.value = null
  }

  fun restartTimer() {
    stopTimer()
    startTimer()
  }
}