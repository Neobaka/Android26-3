package com.example.players.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.players.data.Player
import com.example.players.data.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PlayerDetailsUiState(
    val player: Player? = null,
    val commentsExpanded: Boolean = false
)

class PlayerDetailsViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val playerId: Int = savedStateHandle["playerId"] ?: -1

    private val _state = MutableStateFlow(PlayerDetailsUiState())
    val state: StateFlow<PlayerDetailsUiState> = _state.asStateFlow()

    init {
        _state.value = PlayerDetailsUiState(player = PlayerRepository.getPlayer(playerId))
    }

    fun toggleComments() {
        _state.value = _state.value.copy(commentsExpanded = !_state.value.commentsExpanded)
    }
}
