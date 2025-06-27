package com.voloaccendo.androiddemo.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.ui.viewmodel.PeopleViewModel
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterial3Api
@Composable
fun PeopleListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("People List") },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding -> PeopleList(
        modifier = Modifier.padding(innerPadding),
        navController = navController ) }
}

@Composable
fun PeopleList(modifier: Modifier = Modifier, navController: NavController) {
    val peopleViewModel : PeopleViewModel = koinViewModel()
    // Collect the StateFlows from the ViewModel
    // collectAsStateWithLifecycle is lifecycle-aware and recommended
    val peopleList by peopleViewModel.peopleList.collectAsStateWithLifecycle()
    val isLoading by peopleViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by peopleViewModel.errorMessage.collectAsStateWithLifecycle()

    Box(modifier = modifier.padding(horizontal = 16.dp).fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (peopleList.isEmpty()) {
            Text(
                text = "No people to display.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn {
                items(peopleList) { person ->
                    PersonItem(person = person, navController = navController, onClick = {
                        navController.navigate("personDetails/${person.login.uuid}")
                    })
                }
            }
        }
    }
}

// Assuming you have a Composable for displaying a single Person
@Composable
fun PersonItem(person: Person, navController: NavController, onClick: () -> Unit) {
    val firstName = person.name.first
    val lastName = person.name.last
    val gender = person.gender
    val age = person.dob.age

    Row(modifier = Modifier
        .border(width = Dp.Hairline, color = Color.Gray)
        .padding(8.dp)
        .clickable { onClick() }, // Add padding inside the border
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(person.picture.thumbnail) // Your image URL
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
            modifier = Modifier.size(56.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column(modifier = Modifier.weight(1f)
        ) {
            Text(text = "$firstName $lastName")
            Text(text = "$age $gender")
        }
    }
}
