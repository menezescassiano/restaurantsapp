package com.cassianomenezes.restaurantsapp.database

import com.cassianomenezes.restaurantsapp.internal.StatusConstants
import com.cassianomenezes.restaurantsapp.model.DataResult
import com.cassianomenezes.restaurantsapp.model.Restaurant

interface RestaurantRepository {

    suspend fun insertAll(restaurantObject: RestaurantObject)

    suspend fun getAll(): List<RestaurantObject>

    suspend fun delete(name: String)

    suspend fun findByTitle(id: String): RestaurantObject

    suspend fun getById(id: String): RestaurantObject

    suspend fun getAddedRestaurants(restaurants: List<Restaurant>): DataResult<List<Restaurant>>

    fun getDesiredOrder(restaurants: List<Restaurant>, status: StatusConstants, word: String? = ""): List<Restaurant>
}