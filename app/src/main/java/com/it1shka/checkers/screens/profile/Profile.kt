package com.it1shka.checkers.screens.profile

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Profile() {
  var isEditing by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(10.dp),
    horizontalArrangement = Arrangement.End,
  ) {
    if (isEditing) {
      TextButton(onClick = { /* TODO */ }) {
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
    if (!isEditing) {

    } else {

    }
  }
}