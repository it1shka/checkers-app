package com.it1shka.checkers.components

import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun ConfirmBackHandler(
  title: String,
  text: String,
  onBack: () -> Unit,
) {
  var showAlert by remember { mutableStateOf(false) }
  BackHandler {
    showAlert = true
  }

  if (showAlert) {
    AlertDialog(
      title = {
        Text(title)
      },
      text = {
        Text(text)
      },
      confirmButton = {
        TextButton(
          content = {
            Text("OK")
          },
          onClick = {
            onBack()
            showAlert = false
          }
        )
      },
      dismissButton = {
        Button(
          content = {
            Text("Cancel")
          },
          onClick = { showAlert = false }
        )
      },
      onDismissRequest = { showAlert = false }
    )
  }
}