package com.voloaccendo.androiddemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.data.repository.people.IPeopleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // Backing property for the StateFlow (Mutable)
    private val _peopleList = MutableStateFlow<List<Person>>(emptyList())
    // Publicly exposed StateFlow (Immutable)
    val peopleList: StateFlow<List<Person>> = _peopleList.asStateFlow()

    // Optional: Add a StateFlow for loading state
    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Optional: Add a StateFlow for error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // You might want to fetch people immediately when the ViewModel is created
        fetchFirstPeoplePage() // This will be called when the ViewModel is created.
    }

    fun fetchFirstPeoplePage()
    {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            _errorMessage.value = null // Clear previous error messages
            try
            {
                // Assuming peopleRepository.getPeople(count, page) is a suspend function
                // that returns a List<Person>
                val people = peopleRepository.getPeople(count = PAGE_SIZE, page = 1)
                _peopleList.value += people
            } catch (e: Exception)
            {
                // Handle error, e.g., post a message to the UI
                _errorMessage.value = "Failed to fetch people: ${e.message}"
                // Optionally, you could set an empty list or keep the old data
                // _peopleList.value = emptyList()
            } finally
            {
                _isLoading.value = false // Set loading state to false
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 20 // Number of items to fetch per page
    }
}
