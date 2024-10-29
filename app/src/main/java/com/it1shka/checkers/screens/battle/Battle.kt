package com.it1shka.checkers.screens.battle

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.it1shka.checkers.AppScreen
import com.it1shka.checkers.PersistentStorage
import com.it1shka.checkers.R
import kotlinx.coroutines.launch

enum class BattleModeType() {
  ONLINE,
  OFFLINE,
}

data class BattleMode (
  val type: BattleModeType,
  val title: String,
  val subtitle: String,
  val icon: ImageVector,
)

private val battleModes = listOf(
  BattleMode(
    type = BattleModeType.ONLINE,
    title = "Play Online",
    subtitle = "Online mode",
    icon = Icons.Filled.Search,
  ),
  BattleMode(
    type = BattleModeType.OFFLINE,
    title = "Play with Computer",
    subtitle = "Offline mode",
    icon = Icons.Filled.Home,
  ),
)

enum class BotDifficulty {
  EASY,
  NORMAL,
  HARD,
  INSANE,
}

@Composable fun Battle(navController: NavController) {
  var difficulty by remember { mutableStateOf(BotDifficulty.NORMAL) }
  val context = LocalContext.current
  LaunchedEffect(context) {
    PersistentStorage.getDifficulty(context).collect { value ->
      if (value != null && value in BotDifficulty.entries.map { it.name }) {
        difficulty = BotDifficulty.valueOf(value)
      }
    }
  }
  val coroutineScope = rememberCoroutineScope()
  fun changeDifficulty(newDifficulty: BotDifficulty) {
    difficulty = newDifficulty
    coroutineScope.launch {
      PersistentStorage.saveDifficulty(context, newDifficulty.name)
    }
  }

  fun onBattleStart(type: BattleModeType) {
    when (type) {
      BattleModeType.OFFLINE -> {
        navController.navigate(AppScreen.OFFLINE_BATTLE.name)
      }
      BattleModeType.ONLINE -> {
        navController.navigate(AppScreen.ONLINE_BATTLE.name)
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
    Column (
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