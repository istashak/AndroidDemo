package com.voloaccendo.androiddemo.ui.view.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.voloaccendo.androiddemo.data.models.Person
import com.voloaccendo.androiddemo.ui.viewmodel.PeopleViewModel
import org.koin.androidx.compose.koinViewModel
import com.voloaccendo.androiddemo.R
import com.voloaccendo.androiddemo.ui.theme.LocalDimensions

@ExperimentalMaterial3Api
@Composable
fun PeopleListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
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
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding -> PeopleList(
        modifier = Modifier.padding(innerPadding),
        navController = navController ) }
}

@Composable
fun PeopleList(modifier: Modifier = Modifier, navController: NavController) {
    val dimensions = LocalDimensions.current

    val peopleViewModel : PeopleViewModel = koinViewModel()

    val uiState by peopleViewModel.uiState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState() // State for LazyColumn

    Box(modifier = modifier.padding(horizontal = dimensions.screenPadding).fillMaxSize()) {
        if (uiState.isLoading && uiState.people.isEmpty()) { // Show main loader only if initial load and list is empty
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.errorMessage != null && uiState.people.isEmpty()) { // Show error only if initial load failed and list is empty
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = uiState.errorMessage ?: "An error occurred")
                Spacer(modifier = Modifier.size(8.dp))
                Button(onClick = { peopleViewModel.loadFirstPeoplePage() }) {
                    Text("Retry")
                }
            }
        } else if (uiState.people.isEmpty() && !uiState.isLoading) {
            Text(
                text = "No people to display.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(state = listState) {
                items(uiState.people, key = { person -> person.login.uuid }) { person ->
                    PersonItem(person = person, navController = navController, onClick = {
                        navController.navigate("personDetails/${person.login.uuid}")
                    })
                }

                // Optional: Show a loading indicator at the bottom while loading more
                if (uiState.isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                // Optional: Show an error item if loading more failed
                if (uiState.errorMessage != null && uiState.people.isNotEmpty() && !uiState.isLoadingMore && !uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Error loading more. Pull to refresh or try again later.")
                        }
                    }
                }
            }
        }

        // Pagination trigger logic
        val shouldLoadMore by remember {
            derivedStateOf {
                val layoutInfo = listState.layoutInfo
                val totalItemsCount = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

                // Trigger load more when the user is, for example, 5 items away from the end
                // and not already loading, and there are potentially more items.
                totalItemsCount > 0 && // Make sure there are items
                        lastVisibleItemIndex >= totalItemsCount - 1 - 5 && // Threshold
                        !uiState.isLoadingMore && // Not already loading more
                        !uiState.isLoading && // Not initial loading
                        uiState.canLoadMore // ViewModel says we can load more
            }
        }

        LaunchedEffect(shouldLoadMore) {
            if (shouldLoadMore) {
                peopleViewModel.loadMorePeople()
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
