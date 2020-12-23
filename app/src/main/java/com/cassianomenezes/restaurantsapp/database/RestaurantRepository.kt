package com.cassianomenezes.restaurantsapp.database

import com.cassianomenezes.restaurantsapp.model.DataResult
import com.cassianomenezes.restaurantsapp.model.Restaurant

interface RestaurantRepository {

    suspend fun insertAll(restaurantObject: RestaurantObject)

    suspend fun getAll(): List<RestaurantObject>

    suspend fun delete(restaurantObject: RestaurantObject)

    suspend fun findByTitle(id: String): RestaurantObject

    suspend fun getById(id: String): RestaurantObject

    suspend fun getAddedRestaurants(restaurants: List<Restaurant>): DataResult<List<Restaurant>>
}