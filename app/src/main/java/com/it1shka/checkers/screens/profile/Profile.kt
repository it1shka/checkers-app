package com.it1shka.checkers.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.it1shka.checkers.Preferences
import kotlinx.coroutines.launch

@Composable
fun Profile(
  initialNickname: String,
  initialRating: Int,
  initialRegion: String,
) {
  val context = LocalContext.current

  val nickname by Preferences
    .getNickname(context)
    .collectAsState(initialNickname)

  val rating by Preferences
    .getRating(context)
    .collectAsState(initialRating)

  val region by Preferences
    .getRegion(context)
    .collectAsState(initialRegion)

  var isEditing by remember { mutableStateOf(false) }

  var tempNickname by remember { mutableStateOf(nickname) }
  var tempRegion by remember { mutableStateOf(region) }

  LaunchedEffect(isEditing) {
    tempNickname = nickname
    tempRegion = region
  }

  fun randomizeNickname() {
    tempNickname = generateNickname()
  }

  val coroutineScope = rememberCoroutineScope()
  fun applyChanges() {
    coroutineScope.launch {
      Preferences.saveNickname(context, tempNickname)
      Preferences.saveRegion(context, tempRegion)
      isEditing = false
    }
    Toast
      .makeText(context, "Changes applied", Toast.LENGTH_SHORT)
      .show()
  }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(10.dp),
    horizontalArrangement = Arrangement.End,
  ) {
    if (isEditing) {
      TextButton(onClick = ::applyChanges) {
        Text(
          text = "Apply",
          style = MaterialTheme.typography.bodyLarge,
          modifier = Modifier.padding(horizontal = 8.dp)
        )
        Icon(Icons.Default.Check, contentDescription = "Apply Changes")
      }
    }
    val toggleText =
      if (isEditing) "Discard"
      else "Edit"
    val toggleIcon =
      if (isEditing) Icons.Default.Close
      else Icons.Default.Edit
    TextButton(onClick = { isEditing = !isEditing }) {
      Text(
        text = toggleText,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = 8.dp)
      )
      Icon(toggleIcon, contentDescription = "Toggle Edit")
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.Center,
  ) {
    Column(
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      if (!isEditing) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Icon(Icons.Default.Person, contentDescription = "Nickname")
          Text(
            text = if (nickname.isEmpty()) "Unknown" else nickname,
            style = MaterialTheme.typography.headlineLarge,
          )
        }
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Icon(Icons.Default.Star, contentDescription = "Wins")
          Text(
            text = "$rating online win(s)",
            style = MaterialTheme.typography.headlineSmall,
          )
        }
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          Icon(Icons.Default.LocationOn, contentDescription = "Region")
          val regionString =
            if (region.isEmpty()) "unknown"
            else region
          Text(
            text = "Region: $regionString",
            style = MaterialTheme.typography.headlineSmall,
          )
        }
      } else {
        Text(
          text = "Change to:",
          style = MaterialTheme.typography.headlineSmall,
        )
        TextField(
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          value = tempNickname,
          onValueChange = { tempNickname = it },
          label = {
            Text("Nickname")
          }
        )
        TextField(
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          value = tempRegion,
          onValueChange = { tempRegion = it },
          label = {
            Text("Region")
          }
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceAround,
        ) {
          TextButton(onClick = ::randomizeNickname) {
            Text("Randomize Nickname")
          }
        }
      }
    }
  }
}