package com.voloaccendo.androiddemo.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voloaccendo.androiddemo.data.models.Settings
import com.voloaccendo.androiddemo.data.models.ThemeLighting
import com.voloaccendo.androiddemo.data.repository.settings.ISettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsViewModel(private val settingsRepository: ISettingsRepository) : ViewModel() {

    private val _settings: MutableStateFlow<Settings> = MutableStateFlow(Settings())
    val settings: StateFlow<Settings> = _settings

    val themeLighting: StateFlow<ThemeLighting>

    fun onThemeLightingChanged(themeLighting: ThemeLighting) {
        _settings.value = _settings.value.copy(themeLighting = themeLighting)
        viewModelScope.launch {
            settingsRepository.setThemeLightingSettings(themeLighting)
        }
    }

    fun onSaveSettings() {
        viewModelScope.launch {
            settingsRepository.setSettings(_settings.value)
        }
    }

    init {
        viewModelScope.launch {
            // Load the full settings object once
            val currentSettings = settingsRepository.settings.first()
            _settings.value = currentSettings

            println("settings: $currentSettings")
        }

        // Properly get initial value for themeLighting BEFORE stateIn()
        themeLighting = settingsRepository.themeLightingSettings
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = runBlocking {
                    settingsRepository.themeLightingSettings.first()
                }
            )
    }
}

