package com.cassianomenezes.restaurantsapp.di

import com.cassianomenezes.restaurantsapp.database.RestaurantRepository
import com.cassianomenezes.restaurantsapp.home.view.viewmodel.MainViewModel
import com.cassianomenezes.restaurantsapp.repository.DataRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val repositoryModule = module {
    single { DataRepository(context = get()) }
}

private val viewModelModule = module {
    viewModel { (restaurantRepository: RestaurantRepository) -> MainViewModel(repository = get(), restaurantRepositoryImpl = restaurantRepository ) }
}

fun loadKoinModules() {
    loadKoinModules(listOf(repositoryModule, viewModelModule))
}