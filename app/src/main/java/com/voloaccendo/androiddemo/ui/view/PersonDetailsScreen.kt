package com.voloaccendo.androiddemo.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.voloaccendo.androiddemo.data.model.Person
import com.voloaccendo.androiddemo.ui.viewmodel.PersonDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun PersonDetailsScreen( personId: String, navController: NavController) {
    // Parameters can be passed to koinViewModel using the `parameters` lambda.
    // The `parametersOf` function is a helper to create a ParametersHolder instance.
    val personViewModel: PersonDetailsViewModel = koinViewModel(
        parameters = { parametersOf(personId) }
    )
    // Assuming PersonViewModel has a way to expose the Person details, for example, a StateFlow or LiveData.
    // For this example, let's assume it has a `person` property.
    val person: Person? = personViewModel.person.value // Or observeAsState() if it's a StateFlow

    Scaffold { innerPadding ->
        Column(
            modifier = androidx.compose.ui.Modifier
                .padding(innerPadding)
                .fillMaxSize() // Optional: if you want the column to fill the available space
        ) {
            Text(text = "Name: ${person?.name}")
            Text(text = "Age: ${person?.dob?.age}")
            Text(text = "Email: ${person?.email}")
        }
    }
}
