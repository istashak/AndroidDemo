package com.voloaccendo.androiddemo

import android.app.Application

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

import com.voloaccendo.androiddemo.di.networkModule
import com.voloaccendo.androiddemo.di.repositoryModule
import com.voloaccendo.androiddemo.di.viewModelsModule

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
            // The order here generally does not matter for Koin's dependency resolution,
            // as Koin builds a graph of all definitions and resolves them lazily.
            // However, a logical order (e.g., from core/network to UI/viewModels) can improve readability.
            modules(
                networkModule,
                repositoryModule,
                viewModelsModule)
        }
    }
}
