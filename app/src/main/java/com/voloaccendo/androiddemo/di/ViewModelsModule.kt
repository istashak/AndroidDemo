package com.voloaccendo.androiddemo.di

import com.voloaccendo.androiddemo.ui.viewmodel.PeopleViewModel
import com.voloaccendo.androiddemo.ui.viewmodel.PersonDetailsViewModel
import org.koin.core.module.dsl.viewModel

import org.koin.dsl.module

val viewModelsModule = module {
    viewModel {
        PeopleViewModel(get())
    }
    viewModel { (personId: String) ->
        PersonDetailsViewModel(personId = personId, peopleRepository = get())
    }
}
