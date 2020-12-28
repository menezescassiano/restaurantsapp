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
        return try {
            restaurants.forEach {
                if (restaurantDao.findByTitle(it.name) != null) {
                    it.added = true
                }
            }
            val myRestaurants = getOriginalOrder(restaurants)
            DataResult(RequestStatus.SUCCESS, myRestaurants)
        } catch (e:Exception) {
            print("Error: $e")
            DataResult(status = RequestStatus.ERROR)
        }
    }

    private fun getOriginalOrder(restaurants: List<Restaurant>): List<Restaurant> {
        val allList = arrayListOf<Restaurant>()
        restaurants.run {
            val favOpenList = this.filter { it.status == OPEN.status && it.added }
            val favOrderAheadList = this.filter { it.status == ORDER_AHEAD.status && it.added }
            val favClosedList = this.filter { it.status == CLOSED.status && it.added }

            val openList = this.filter { it.status == OPEN.status && !it.added}
            val closedList = this.filter { it.status == CLOSED.status && !it.added}
            val orderAhead = this.filter { it.status == ORDER_AHEAD.status && !it.added }
            allList.run {
                addAll(favOpenList)
                addAll(favOrderAheadList)
                addAll(favClosedList)
                addAll(openList)
                addAll(orderAhead)
                addAll(closedList)
            }
        }
        return allList
    }

}