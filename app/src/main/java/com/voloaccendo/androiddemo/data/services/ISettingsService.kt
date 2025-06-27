package com.voloaccendo.androiddemo.data.services

import com.voloaccendo.androiddemo.data.models.Settings
import com.voloaccendo.androiddemo.data.models.ThemeLighting

interface ISettingsService {
    fun getSettings(): Settings
    fun setSettings(settings: Settings)

    fun getThemeLightingSettings(): ThemeLighting
    fun setThemeLightingSettings(themeLighting: ThemeLighting)
}
