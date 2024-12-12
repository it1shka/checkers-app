package com.it1shka.checkers.components

import android.app.AlertDialog
import android.content.Context

fun withConfirmation(
  context: Context,
  title: String = "Are you sure?",
  message: String,
  action: () -> Unit,
) {
  AlertDialog.Builder(context)
    .setTitle(title)
    .setMessage(message)
    .setPositiveButton("OK") { _, _ ->
      action()
    }
    .setNegativeButton("Cancel", null)
    .show()
}