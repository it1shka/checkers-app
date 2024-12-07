package com.it1shka.checkers.screens.offline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuActions (
  val onLeaveBattle: () -> Unit,
  val onRestart: () -> Unit,
  val onColorChange: () -> Unit,
) {
  companion object {
    fun empty() = MenuActions(
      onLeaveBattle = {},
      onRestart = {},
      onColorChange = {},
    )
  }
}

private data class MenuItem (
  val title: String,
  val onClick: () -> Unit,
  val icon: ImageVector
)

private fun menuItems(actions: MenuActions) = listOf(
  MenuItem(
    title = "Leave battle",
    onClick = { actions.onLeaveBattle() },
    icon = Icons.Filled.Close,
  ),
  MenuItem(
    title = "Restart",
    onClick = { actions.onRestart() },
    icon = Icons.Filled.Refresh,
  ),
  MenuItem(
    title = "Change color",
    onClick = { actions.onColorChange() },
    icon = Icons.Filled.Edit,
  )
)

@Composable
fun OfflineDropdownMenu(actions: MenuActions = MenuActions.empty()) {
  var menuExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.End,
    verticalArrangement = Arrangement.Top,
  ) {
    IconButton(onClick = { menuExpanded = !menuExpanded }) {
      Icon(
        Icons.Filled.MoreVert,
        contentDescription = "Dropdown Menu"
      )
      DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false }
      ) {
        menuItems(actions).map { menuItem ->
          DropdownMenuItem(
            text = { Text(menuItem.title) },
            onClick = { menuItem.onClick() },
            leadingIcon = {
              Icon(
                menuItem.icon,
                contentDescription = menuItem.title
              )
            }
          )
        }
      }
    }
  }
}