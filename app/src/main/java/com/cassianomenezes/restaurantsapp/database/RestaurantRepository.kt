package com.cassianomenezes.restaurantsapp.database

interface RestaurantRepository {

    suspend fun insertAll(restaurantObject: RestaurantObject)

    suspend fun getAll(): List<RestaurantObject>

    suspend fun delete(restaurantObject: RestaurantObject)

    suspend fun findByTitle(id: String): RestaurantObject

    suspend fun getById(id: String): RestaurantObject
}