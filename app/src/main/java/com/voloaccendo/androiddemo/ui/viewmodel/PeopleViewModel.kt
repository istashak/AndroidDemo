package com.voloaccendo.androiddemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.data.repository.people.IPeopleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PeopleUiState(
    val people: List<Person> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false, // For pagination
    val errorMessage: String? = null,
    val currentPage: Int = 1, // Start with page 1
    val canLoadMore: Boolean = true // Flag to indicate if more items can be loaded
)

class PeopleViewModel(private val peopleRepository: IPeopleRepository) : ViewModel(){

    private val _uiState = MutableStateFlow(PeopleUiState())
    val uiState: StateFlow<PeopleUiState> = _uiState.asStateFlow()

    init {
        // You might want to fetch people immediately when the ViewModel is created
        loadFirstPeoplePage() // This will be called when the ViewModel is created.
    }

    fun loadFirstPeoplePage()
    {
        if (_uiState.value.isLoading) return // Prevent multiple initial loads

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, currentPage = 1, people = emptyList(), canLoadMore = true) }
            try {
                // Replace with your actual repository call
                val newPeople = peopleRepository.getPeople(page = 1, count = PAGE_SIZE)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        people = newPeople,
                        currentPage = 1,
                        canLoadMore = newPeople.size == PAGE_SIZE // Assume more if full page loaded
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun loadMorePeople() {
        val currentState = _uiState.value
        // Prevent multiple loads if already loading, or if no more items, or initial load in progress
        if (currentState.isLoadingMore || !currentState.canLoadMore || currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            try {
                val nextPage = currentState.currentPage + 1
                // Replace with your actual repository call
                val additionalPeople = peopleRepository.getPeople(page = nextPage, count = PAGE_SIZE)

                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        people = it.people + additionalPeople, // Append new people
                        currentPage = nextPage,
                        canLoadMore = additionalPeople.size == PAGE_SIZE // Still more if full page loaded
                    )
                }
            } catch (e: Exception) {
                // Handle error for loading more, maybe show a toast or a small error item in the list
                _uiState.update { it.copy(isLoadingMore = false, errorMessage = "Failed to load more: ${e.message}") }
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 20 // Number of items to fetch per page
    }
}
