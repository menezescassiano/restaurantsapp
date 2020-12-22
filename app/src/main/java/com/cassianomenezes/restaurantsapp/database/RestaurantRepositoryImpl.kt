package com.cassianomenezes.restaurantsapp.database

import com.cassianomenezes.restaurantsapp.model.Restaurant

class RestaurantRepositoryImpl(private val restaurantDao: RestaurantDao): RestaurantRepository {

    override suspend fun insertAll(restaurantObject: RestaurantObject) {
        restaurantDao.insertAll(RestaurantObject(id = restaurantObject.id, title = restaurantObject.title))
    }

    override suspend fun getAll(): List<RestaurantObject> {
        return restaurantDao.getAll()
    }

    override suspend fun delete(restaurantObject: RestaurantObject) {
        restaurantDao.delete(restaurantObject)
    }

    override suspend fun findByTitle(id: String): RestaurantObject {
        return restaurantDao.getById(id)
    }

    override suspend fun getById(id: String): RestaurantObject {
        return restaurantDao.getById(id)
    }

}