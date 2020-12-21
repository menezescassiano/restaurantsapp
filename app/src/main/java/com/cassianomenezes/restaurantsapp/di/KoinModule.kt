package com.cassianomenezes.restaurantsapp.di

import com.cassianomenezes.restaurantsapp.home.view.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/*private val serviceModule = module {
    single { RetrofitClient.getApiService() }
}

private val repositoryModule = module {
    single { DataRepository(service = get()) }
}

private val resourceManager = module {
    single { ResourceManager(context = get()) }
}*/

private val viewModelModule = module {
    viewModel { MainViewModel() }
    /*viewModel { RecipeDetailViewModel(resourceManager = get()) }*/
}

fun loadKoinModules() {
    loadKoinModules(listOf(viewModelModule))
}