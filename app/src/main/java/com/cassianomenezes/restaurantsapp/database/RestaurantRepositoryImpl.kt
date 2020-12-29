package com.cassianomenezes.restaurantsapp.database

import com.cassianomenezes.restaurantsapp.internal.StatusConstants.*
import com.cassianomenezes.restaurantsapp.internal.RequestStatus
import com.cassianomenezes.restaurantsapp.internal.StatusConstants
import com.cassianomenezes.restaurantsapp.model.DataResult
import com.cassianomenezes.restaurantsapp.model.Restaurant

class RestaurantRepositoryImpl(private val restaurantDao: RestaurantDao) : RestaurantRepository {

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

    override suspend fun getAddedRestaurants(restaurants: List<Restaurant>): DataResult<List<Restaurant>> {
        return try {
            restaurants.forEach {
                if (restaurantDao.findByTitle(it.name) != null) {
                    it.added = true
                }
            }
            DataResult(RequestStatus.SUCCESS, getDesiredOrder(restaurants, INITIAL))
        } catch (e: Exception) {
            print("Error: $e")
            DataResult(status = RequestStatus.ERROR)
        }
    }

    override fun getDesiredOrder(restaurants: List<Restaurant>, status: StatusConstants, word: String?): List<Restaurant> {
        val allList = arrayListOf<Restaurant>()
        restaurants.run {
            val favOpenList = this.filter { it.status == OPEN.status && it.added }
            val favOrderAheadList = this.filter { it.status == ORDER_AHEAD.status && it.added }
            val favClosedList = this.filter { it.status == CLOSED.status && it.added }
            val openList = this.filter { it.status == OPEN.status && !it.added }
            val closedList = this.filter { it.status == CLOSED.status && !it.added }
            val orderAhead = this.filter { it.status == ORDER_AHEAD.status && !it.added }

            allList.run {
                when (status) {
                    OPEN -> {
                        addAll(favOpenList)
                        addAll(openList)
                        addAll(favOrderAheadList)
                        addAll(favClosedList)
                        addAll(orderAhead)
                        addAll(closedList)
                    }
                    CLOSED -> {
                        addAll(favClosedList)
                        addAll(closedList)
                        addAll(favOpenList)
                        addAll(favOrderAheadList)
                        addAll(openList)
                        addAll(orderAhead)
                    }
                    ORDER_AHEAD -> {
                        addAll(favOrderAheadList)
                        addAll(orderAhead)
                        addAll(favOpenList)
                        addAll(favClosedList)
                        addAll(openList)
                        addAll(closedList)
                    }
                    BEST_MATCH -> addAll(restaurants.sortedBy { it.sortingValues.bestMatch }.reversed())
                    NEWEST -> addAll(restaurants.sortedBy { it.sortingValues.newest }.reversed())
                    RATING_AVERAGE -> addAll(restaurants.sortedBy { it.sortingValues.ratingAverage }.reversed())
                    DISTANCE -> addAll(restaurants.sortedBy { it.sortingValues.distance })
                    POPULARITY -> addAll(restaurants.sortedBy { it.sortingValues.popularity }.reversed())
                    AVERAGE_PRICE -> addAll(restaurants.sortedBy { it.sortingValues.averageProductPrice })
                    DELIVERY_COSTS -> addAll(restaurants.sortedBy { it.sortingValues.deliveryCosts })
                    MIN_COSTS -> addAll(restaurants.sortedBy { it.sortingValues.minCost })
                    WORDS -> {
                        when {
                            !word.isNullOrEmpty() -> addAll(restaurants.filter { s -> s.name.contains(word, ignoreCase = true) })
                            else -> {
                                addAll(favOpenList)
                                addAll(favOrderAheadList)
                                addAll(favClosedList)
                                addAll(openList)
                                addAll(orderAhead)
                                addAll(closedList)
                            }
                        }
                    }

                    else -> {
                        addAll(favOpenList)
                        addAll(favOrderAheadList)
                        addAll(favClosedList)
                        addAll(openList)
                        addAll(orderAhead)
                        addAll(closedList)
                    }
                }
            }
        }
        return allList
    }

}