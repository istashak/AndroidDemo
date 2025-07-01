package com.voloaccendo.androiddemo.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.voloaccendo.androiddemo.data.repository.IPeopleRepository
import com.voloaccendo.androiddemo.data.repository.PeopleRepository
import com.voloaccendo.androiddemo.data.repository.settings.DataStoreSettingsRepository
import com.voloaccendo.androiddemo.data.repository.settings.ISettingsRepository
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore(DataStoreSettingsRepository.SETTINGS_DATA_STORE)

val repositoryModule = module {
    single<IPeopleRepository> {
        PeopleRepository(get())
    }
    single<ISettingsRepository> {
        DataStoreSettingsRepository(get<Context>().dataStore)
    }
}
