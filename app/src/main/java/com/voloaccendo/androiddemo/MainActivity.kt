package com.voloaccendo.androiddemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.voloaccendo.androiddemo.ui.theme.AndroidDemoTheme
import com.voloaccendo.androiddemo.ui.view.screens.PeopleListScreen
import com.voloaccendo.androiddemo.ui.view.screens.PersonDetailsScreen
import com.voloaccendo.androiddemo.ui.view.screens.SettingsScreen
import com.voloaccendo.androiddemo.ui.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Use settingsRepository instance here
        setContent {
            val settingsViewModel: SettingsViewModel = koinViewModel()
            AndroidDemoTheme(themeLighting = settingsViewModel.themeLighting) {
                AppContent()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "peopleList") {
        composable("peopleList") {
            PeopleListScreen(navController = navController)
        }
        composable("personDetails/{personId}") { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId")
            // Or getInt, getLong etc. depending on your ID type
            if (personId != null) {
                PersonDetailsScreen(navController = navController, personId = personId)
            }
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    AppContent()
}
