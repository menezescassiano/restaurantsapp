package com.cassianomenezes.restaurantsapp.home.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cassianomenezes.restaurantsapp.model.OverallData
import com.cassianomenezes.restaurantsapp.repository.DataRepository

class MainViewModel(val repository: DataRepository) : ViewModel() {

    val restaurantData = MutableLiveData<OverallData>()

    fun getData() {
        restaurantData.postValue(repository.getRestaurants())
    }
}