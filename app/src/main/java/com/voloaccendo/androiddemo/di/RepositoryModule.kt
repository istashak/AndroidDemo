package com.voloaccendo.androiddemo.di

import com.voloaccendo.androiddemo.data.repository.IPeopleRepository
import com.voloaccendo.androiddemo.data.repository.PeopleRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<IPeopleRepository> {
        PeopleRepository(get())
    }
}
