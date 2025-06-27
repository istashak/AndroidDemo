package com.voloaccendo.androiddemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.voloaccendo.androiddemo.ui.theme.AndroidDemoTheme
import com.voloaccendo.androiddemo.ui.view.PeopleListScreen
import com.voloaccendo.androiddemo.ui.view.PersonDetailsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidDemoTheme {
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    AppContent()
}
