package com.voloaccendo.androiddemo.ui.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.voloaccendo.androiddemo.R
import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.ui.viewmodel.PersonDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
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
    val gender = person?.gender?.lowercase()?.replaceFirstChar { it.uppercase() }
    val age = person?.dob?.age.toString()
    val phone = person?.phone
    val email = person?.email
    val streetAddress = "${person?.location?.street?.number} ${person?.location?.street?.name}"
    val cityStateZip = "${person?.location?.city}, ${person?.location?.state} ${person?.location?.postcode}"

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Icon Button",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = { Text(text = stringResource(id = R.string.people))},
                actions = {
                    IconButton(onClick = {
                        navController.navigate("settings")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings Icon Button",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
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
            Text(text = name, modifier = Modifier.padding(top = 30.dp), fontSize = 24.sp)
            Text(text = "$age $gender", modifier = Modifier.padding(top = 4.dp))
            Text(text = "$email", modifier = Modifier.padding(top = 4.dp))
            Text(text = "$phone", modifier = Modifier.padding(top = 4.dp))
            Column (modifier = Modifier.padding(8.dp)) {
                Text(text = streetAddress)
                Text(text = cityStateZip)
            }
        }
    }
}
