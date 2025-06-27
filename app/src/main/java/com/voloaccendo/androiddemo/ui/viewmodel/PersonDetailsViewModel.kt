package com.voloaccendo.androiddemo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.data.repository.IPeopleRepository
import kotlinx.coroutines.launch

/**
 * In the PersonDetailsViewModel I demonstrate the use of LiveData. Though LiveData isn't a robust as StateFlows,
 * it is still a viable option that is also lifecycle aware out of the box.
 */
class PersonDetailsViewModel(private val personId : String,
                             private val peopleRepository: IPeopleRepository) : ViewModel() {

    private val _person = MutableLiveData<Person>()
    val person: LiveData<Person> = _person

    init {
        viewModelScope.launch {
            _person.value = peopleRepository.getPerson(personId)
        }
    }
}

