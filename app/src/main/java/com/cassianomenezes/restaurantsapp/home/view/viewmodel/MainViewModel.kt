package com.cassianomenezes.restaurantsapp.home.view.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.cassianomenezes.restaurantsapp.database.RestaurantObject
import com.cassianomenezes.restaurantsapp.database.RestaurantRepository
import com.cassianomenezes.restaurantsapp.internal.StatusConstants
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.model.Restaurant
import com.cassianomenezes.restaurantsapp.repository.DataRepository
import kotlinx.coroutines.launch

class MainViewModel(val repository: DataRepository, val restaurantRepositoryImpl: RestaurantRepository) : ViewModel(), LifecycleObserver {

    val restaurantData = MutableLiveData<OverallData>()
    val listData = MutableLiveData<List<Restaurant>>()
    var running = ObservableBoolean(false)
    var inputTextSearch = ObservableField("")

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun getData() {
        running.set(true)
        restaurantData.postValue(getOverallData())
        running.set(false)
    }

    fun getOverallData(): OverallData {
        return repository.getRestaurants()
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

    fun getDesiredOrder(word: String? = "", restaurants: List<Restaurant>, status: StatusConstants): List<Restaurant> {
        return restaurantRepositoryImpl.getDesiredOrder(restaurants, status, word)
    }
}