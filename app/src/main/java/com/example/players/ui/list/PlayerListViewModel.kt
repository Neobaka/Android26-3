package com.example.players.ui.list

import androidx.lifecycle.ViewModel
import com.example.players.data.Player
import com.example.players.data.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlayerListUiState(val players: List<Player> = emptyList())

class PlayerListViewModel : ViewModel() {

    private val _state = MutableStateFlow(PlayerListUiState())
    val state: StateFlow<PlayerListUiState> = _state.asStateFlow()

    init {
        _state.value = PlayerListUiState(PlayerRepository.getPlayers())
    }
}
