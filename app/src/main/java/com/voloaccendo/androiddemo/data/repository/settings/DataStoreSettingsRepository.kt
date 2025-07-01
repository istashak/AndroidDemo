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


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreSettingsRepository.SETTINGS_DATA_STORE)

class DataStoreSettingsRepository(private val context: Context): ISettingsRepository {
    private val themeLightingKey = stringPreferencesKey(THEME_LIGHTING_PREF)

    override val settings: Flow<Settings> = context.dataStore.data
        .map { preferences ->
            val themeName = preferences[themeLightingKey] ?: ThemeLighting.SYSTEM.name
            Settings(ThemeLighting.valueOf(value = themeName))
        }

    suspend override fun setSettings(settings: Settings) {
        setThemeLightingSettings(settings.themeLighting)
    }

    override val themeLightingSettings = context.dataStore.data
        .map { preferences ->
            val themeName = preferences[themeLightingKey] ?: ThemeLighting.SYSTEM.name
            ThemeLighting.valueOf(value = themeName)
        }

    override suspend fun setThemeLightingSettings(themeLighting: ThemeLighting) {
        context.dataStore.edit { preferences ->
            preferences[themeLightingKey] = themeLighting.name
        }
    }

    companion object {
        const val SETTINGS_DATA_STORE = "settings-data-store"
        const val THEME_LIGHTING_PREF = "theme-lighting-pref"
    }
}
