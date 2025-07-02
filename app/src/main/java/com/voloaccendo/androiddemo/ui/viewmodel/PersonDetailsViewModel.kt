package com.voloaccendo.androiddemo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.data.repository.people.IPeopleRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class PersonDetailsViewState(val person: Person) {
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("(MM/dd/yyyy)", Locale.getDefault())
    val name = "${person.name.first} ${person.name.last}"
    val ageDobGender: String
        get() {
            val gender = person.gender.lowercase(Locale.getDefault()).replaceFirstChar { it.uppercase(Locale.getDefault()) }

            val dob = inputFormat.parse(person.dob.date)
            val formattedDate = outputFormat.format(dob!!)
            return "${person.dob.age} $formattedDate, $gender"
        }

    val email = person.email
    val homePhone = person.phone
    val mobilePhone = person.cell
    val streetAddress = "${person.location.street.number} ${person.location.street.name}"
    val cityStateZip = "${person.location.city}, ${person.location.state} ${person.location.postcode}"
    val pictureUrl = person.picture.large
}
/**
 * In the PersonDetailsViewModel I demonstrate the use of LiveData. Though LiveData isn't a robust as StateFlows,
 * it is still a viable option that is also lifecycle aware out of the box.
 */
class PersonDetailsViewModel(private val personId : String,
                             private val peopleRepository: IPeopleRepository) : ViewModel() {

    private val _personDetails = MutableLiveData<PersonDetailsViewState>()
    val personDetails: LiveData<PersonDetailsViewState> = _personDetails

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _personDetails.value = PersonDetailsViewState(peopleRepository.getPerson(personId))
            _isLoading.value = false
        }
    }
}

