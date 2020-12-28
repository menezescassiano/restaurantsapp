package com.cassianomenezes.restaurantsapp.database

import com.cassianomenezes.restaurantsapp.internal.Constants.*
import com.cassianomenezes.restaurantsapp.internal.RequestStatus
import com.cassianomenezes.restaurantsapp.model.DataResult
import com.cassianomenezes.restaurantsapp.model.Restaurant

class RestaurantRepositoryImpl(private val restaurantDao: RestaurantDao): RestaurantRepository {

    override suspend fun insertAll(restaurantObject: RestaurantObject) {
        restaurantDao.insertAll(RestaurantObject(id = restaurantObject.id, title = restaurantObject.title))
    }

    override suspend fun getAll(): List<RestaurantObject> {
        return restaurantDao.getAll()
    }

    override suspend fun delete(name: String) {
        restaurantDao.delete(name)
    }

    override suspend fun findByTitle(id: String): RestaurantObject {
        return restaurantDao.getById(id)
    }

    override suspend fun getById(id: String): RestaurantObject {
        return restaurantDao.getById(id)
    }

    override suspend fun getAddedRestaurants(restaurants: List<Restaurant>) : DataResult<List<Restaurant>> {
         val myRestaurants = getOriginalOrder(restaurants)
        return try {
            myRestaurants.forEach {
                if (restaurantDao.findByTitle(it.name) != null) {
                    it.added = true
                }
            }
            DataResult(RequestStatus.SUCCESS, myRestaurants)
        } catch (e:Exception) {
            print("Error: $e")
            DataResult(status = RequestStatus.ERROR)
        }
    }

    private fun getOriginalOrder(restaurants: List<Restaurant>): List<Restaurant> {
        val allList = arrayListOf<Restaurant>()
        restaurants.run {
            val openList = this.filter { it.status == OPEN.status }
            val closedList = this.filter { it.status == CLOSED.status }
            val orderAhead = this.filter { it.status == ORDER_AHEAD.status }
            allList.run {
                addAll(openList)
                addAll(orderAhead)
                addAll(closedList)
            }
        }
        return allList
    }

}