package com.example.players.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.players.data.Player

val AppOrange = Color(0xFFFF9800)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerListScreen(
    onPlayerClick: (Int) -> Unit,
    viewModel: PlayerListViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* TODO: добавление */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Добавить")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppOrange,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(state.players, key = { it.id }) { player ->
                PlayerItem(player, onClick = { onPlayerClick(player.id) })
            }
        }
    }
}

@Composable
private fun PlayerItem(player: Player, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(size = 48)
        Spacer(Modifier.width(8.dp))
        Text(
            text = player.name,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = player.number.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/** Заглушка вместо фото (позже заменится на загрузку по URL). */
@Composable
fun Avatar(size: Int) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .then(Modifier),
        contentAlignment = Alignment.Center
    ) {
        Surface(color = AppOrange, modifier = Modifier.fillMaxSize()) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding((size / 6).dp)
            )
        }
    }
}
