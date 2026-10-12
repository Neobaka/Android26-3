package com.example.players.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.players.App
import com.example.players.domain.model.Product
import com.example.players.domain.usecase.GetProductDetailsUseCase
import com.example.players.presentation.common.toUiMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(val product: Product, val reviewsExpanded: Boolean = false) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}

class ProductDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getProductDetails: GetProductDetailsUseCase
) : ViewModel() {

    private val productId: Int = savedStateHandle["productId"] ?: -1

    private val _state = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val state: StateFlow<DetailsUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = DetailsUiState.Loading
        viewModelScope.launch {
            getProductDetails(productId).fold(
                onSuccess = { _state.value = DetailsUiState.Success(it) },
                onFailure = { _state.value = DetailsUiState.Error(it.toUiMessage()) }
            )
        }
    }

    fun toggleReviews() {
        _state.update { s ->
            if (s is DetailsUiState.Success) s.copy(reviewsExpanded = !s.reviewsExpanded) else s
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as App
                ProductDetailsViewModel(createSavedStateHandle(), app.container.getProductDetailsUseCase)
            }
        }
    }
}
