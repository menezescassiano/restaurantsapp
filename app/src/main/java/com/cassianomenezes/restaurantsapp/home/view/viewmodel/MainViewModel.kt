package com.cassianomenezes.restaurantsapp.home.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassianomenezes.restaurantsapp.database.RestaurantObject
import com.cassianomenezes.restaurantsapp.database.RestaurantRepository
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.model.Restaurant
import com.cassianomenezes.restaurantsapp.repository.DataRepository
import kotlinx.coroutines.launch

class MainViewModel(val repository: DataRepository, val restaurantRepositoryImpl: RestaurantRepository) : ViewModel() {

    val restaurantData = MutableLiveData<OverallData>()
    val listData = MutableLiveData<List<Restaurant>>()

    fun getData() {
        restaurantData.postValue(repository.getRestaurants())
    }

    fun handleList(restaurants: List<Restaurant>) {
        viewModelScope.launch {
            restaurantRepositoryImpl.getAddedRestaurants(restaurants).run {
                listData.value = this.data
            }
        }
    }

    fun favRestaurant(name: String, added: Boolean) {
        viewModelScope.launch {
            when {
                added -> restaurantRepositoryImpl.delete(name)
                else -> restaurantRepositoryImpl.insertAll(RestaurantObject(null, name))
            }
        }
    }
}