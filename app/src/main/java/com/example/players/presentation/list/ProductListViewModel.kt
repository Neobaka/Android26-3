package com.example.players.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.players.App
import com.example.players.domain.model.Product
import com.example.players.domain.usecase.GetProductsUseCase
import com.example.players.presentation.common.toUiMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductListUiState(
    val query: String = "",
    val items: List<Product> = emptyList(),
    val isLoading: Boolean = false,       // первая загрузка / новый поиск
    val isLoadingMore: Boolean = false,   // подгрузка следующей страницы
    val error: String? = null,
    val endReached: Boolean = false
)

class ProductListViewModel(
    private val getProducts: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListUiState())
    val state: StateFlow<ProductListUiState> = _state.asStateFlow()

    private var loadJob: Job? = null
    private var searchJob: Job? = null

    init {
        loadFirstPage()
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MS) // не дёргаем сеть на каждую букву
            loadFirstPage()
        }
    }

    fun retry() {
        if (_state.value.items.isEmpty()) {
            loadFirstPage()
        } else {
            _state.update { it.copy(error = null) }
            loadNextPage()
        }
    }

    fun loadNextPage() {
        val s = _state.value
        if (s.isLoading || s.isLoadingMore || s.endReached || s.error != null || s.items.isEmpty()) return

        _state.update { it.copy(isLoadingMore = true) }
        loadJob = viewModelScope.launch {
            getProducts(s.query, PAGE_SIZE, s.items.size).fold(
                onSuccess = { page ->
                    _state.update {
                        val all = it.items + page.items
                        it.copy(
                            items = all,
                            isLoadingMore = false,
                            endReached = page.items.isEmpty() || all.size >= page.total
                        )
                    }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoadingMore = false, error = e.toUiMessage()) }
                }
            )
        }
    }

    private fun loadFirstPage() {
        loadJob?.cancel()
        val query = _state.value.query
        _state.update {
            it.copy(isLoading = true, isLoadingMore = false, error = null, items = emptyList(), endReached = false)
        }
        loadJob = viewModelScope.launch {
            getProducts(query, PAGE_SIZE, 0).fold(
                onSuccess = { page ->
                    _state.update {
                        it.copy(
                            items = page.items,
                            isLoading = false,
                            endReached = page.items.size >= page.total
                        )
                    }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.toUiMessage()) }
                }
            )
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val SEARCH_DEBOUNCE_MS = 400L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as App
                ProductListViewModel(app.container.getProductsUseCase)
            }
        }
    }
}
