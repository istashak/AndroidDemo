package com.voloaccendo.androiddemo.ui.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.voloaccendo.androiddemo.ui.theme.LocalDimensions
import com.voloaccendo.androiddemo.ui.view.components.ThemeLightingRadioButton
import com.voloaccendo.androiddemo.ui.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController)  {

    val settingsViewModel: SettingsViewModel = koinViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                actions = {
                    Button(
                        onClick = {
                            settingsViewModel.onSaveSettings()
                            navController.popBackStack()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                        )
                    ) {
                        Text(text = "Done",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary)
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            )
        },
        modifier = Modifier.fillMaxSize()) {
        innerPadding ->
        settingsContent(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            settingsViewModel = settingsViewModel)
    }
}

@Composable
fun settingsContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    settingsViewModel: SettingsViewModel) {

    val dimensions = LocalDimensions.current
    val themeLighting by settingsViewModel.themeLighting.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensions.screenPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Lighting Theme: ${themeLighting.name}")
        ThemeLightingRadioButton(themeLighting = themeLighting, onThemeChanged = { newThemeLighting ->
            settingsViewModel.onThemeLightingChanged(newThemeLighting)
        })
    }
}
