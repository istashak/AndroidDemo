package com.voloaccendo.androiddemo.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.voloaccendo.androiddemo.data.models.Person
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
    val name = "${person?.name?.first} ${person?.name?.last}"
    val gender = person?.gender
    val age = person?.dob?.age.toString()
    val email = person?.email

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize() // Optional: if you want the column to fill the available space
                .padding(16.dp), // Add padding around the Column content
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(person?.picture?.large) // Your ima   ge URL
                    .crossfade(true) // Optional: for smooth transition
                    // .placeholder(R.drawable.placeholder_image) // Optional: placeholder
                    // .error(R.drawable.error_image) // Optional: error image
                    .transformations(
                        // Example: coil.transform.CircleCropTransformation()
                        // Or for resizing to a thumbnail size if the original is large:
                        // coil.transform.ResizeTransformation(width = 100, height = 100)
                    )
                    .build(),
                contentDescription = "Person Image",
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(text = name, modifier = Modifier.padding(top = 30.dp))
            Text(text = "${person?.dob?.age} $gender", modifier = Modifier.padding(top = 4.dp))
            Text(text = "Email: ${person?.email}", modifier = Modifier.padding(top = 4.dp))
        }
    }
}
