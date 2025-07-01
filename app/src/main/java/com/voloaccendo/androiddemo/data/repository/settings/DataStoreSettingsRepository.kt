package com.voloaccendo.androiddemo.data.repository.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.voloaccendo.androiddemo.data.models.Settings
import com.voloaccendo.androiddemo.data.models.ThemeLighting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreSettingsRepository(
    private val dataStore: DataStore<Preferences> // Inject DataStore directly
) : ISettingsRepository {
    private val themeLightingKey = stringPreferencesKey(THEME_LIGHTING_PREF)

    override val settings: Flow<Settings> = dataStore.data // Use the injected dataStore
        .map { preferences ->
            val themeName = preferences[themeLightingKey] ?: ThemeLighting.SYSTEM.name
            Settings(ThemeLighting.valueOf(value = themeName))
        }

    suspend override fun setSettings(settings: Settings) {
        setThemeLightingSettings(settings.themeLighting)
    }

    override val themeLightingSettings = dataStore.data // Use the injected dataStore
        .map { preferences ->
            val themeName = preferences[themeLightingKey] ?: ThemeLighting.SYSTEM.name
            ThemeLighting.valueOf(value = themeName)
        }

    override suspend fun setThemeLightingSettings(themeLighting: ThemeLighting) {
        dataStore.edit { preferences -> // Use the injected dataStore
            preferences[themeLightingKey] = themeLighting.name
        }
    }

    companion object {
        const val SETTINGS_DATA_STORE = "settings-data-store"
        const val THEME_LIGHTING_PREF = "theme-lighting-pref"
    }
}
