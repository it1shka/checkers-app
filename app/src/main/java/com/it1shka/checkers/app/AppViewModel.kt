package com.it1shka.checkers.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
  private val _screen = MutableStateFlow(AppScreen.BATTLE)
  val screen: StateFlow<AppScreen> = _screen.asStateFlow()

  fun navigateTo(newScreen: AppScreen) {
    _screen.update { newScreen }
  }
}