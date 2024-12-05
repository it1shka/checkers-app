package com.it1shka.checkers.screens.battle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.it1shka.checkers.Preferences
import com.it1shka.checkers.R
import com.it1shka.checkers.app.AppScreen
import kotlinx.coroutines.launch

@Composable
fun Battle(nav: NavController) {
  val context = LocalContext.current

  val difficulty by Preferences
    .getDifficulty(context)
    .collectAsState(BotDifficulty.NORMAL.name)

  val coroutineScope = rememberCoroutineScope()
  fun changeDifficulty(newDifficulty: BotDifficulty) {
    coroutineScope.launch {
      Preferences.saveDifficulty(context, newDifficulty.name)
    }
  }

  fun onBattleStart(type: BattleModeType) {
    when (type) {
      BattleModeType.OFFLINE -> {
        nav.navigate(AppScreen.OFFLINE_BATTLE.name)
      }

      BattleModeType.ONLINE -> {
        nav.navigate(AppScreen.ONLINE_BATTLE.name)
      }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(top = 40.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = "Checkers Online",
      style = MaterialTheme.typography.headlineLarge,
    )
    Image(
      modifier = Modifier
        .padding(top = 20.dp, bottom = 20.dp),
      painter = painterResource(id = R.drawable.checkers_banner),
      contentDescription = "Checkers Online Banner",
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp, end = 20.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      battleModes.forEach {
        BattleModeCard(
          mode = it,
          onClick = { onBattleStart(it.type) }
        )
      }
      DifficultySelect(
        difficulty = difficulty,
        changeDifficulty = { changeDifficulty(it) }
      )
    }
  }
}