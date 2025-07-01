package com.voloaccendo.androiddemo.data.repository.settings

import com.voloaccendo.androiddemo.data.models.Settings
import com.voloaccendo.androiddemo.data.models.ThemeLighting
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    /**
     * A Flow that allows for observing any settings change
     */
    val settings: Flow<Settings>

    /**
     * Allow for updating the settings as a whole
     */
    suspend fun setSettings(settings: Settings)

    /**
     * A Flow that allows for observing the theme lighting settings
     */
    val themeLightingSettings: Flow<ThemeLighting>

    /**
     *  Allow for updating the theme lighting settings
     */
    suspend fun setThemeLightingSettings(themeLighting: ThemeLighting)
}
