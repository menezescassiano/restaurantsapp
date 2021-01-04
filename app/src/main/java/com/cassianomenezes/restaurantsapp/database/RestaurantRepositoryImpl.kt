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
            val notFavorited = this.filter { !it.added }

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
                    BEST_MATCH -> {
                        addAll(favOpenList.sortedBy { it.sortingValues.bestMatch }.reversed())
                        addAll(favOrderAheadList.sortedBy { it.sortingValues.bestMatch }.reversed())
                        addAll(favClosedList.sortedBy { it.sortingValues.bestMatch }.reversed())
                        addAll(notFavorited)
                    }
                    NEWEST -> {
                        addAll(favOpenList.sortedBy { it.sortingValues.newest }.reversed())
                        addAll(favOrderAheadList.sortedBy { it.sortingValues.newest }.reversed())
                        addAll(favClosedList.sortedBy { it.sortingValues.newest }.reversed())
                        addAll(notFavorited)
                    }
                    RATING_AVERAGE -> {
                        addAll(favOpenList.sortedBy { it.sortingValues.ratingAverage }.reversed())
                        addAll(favOrderAheadList.sortedBy { it.sortingValues.ratingAverage }.reversed())
                        addAll(favClosedList.sortedBy { it.sortingValues.ratingAverage }.reversed())
                        addAll(notFavorited)
                    }
                    DISTANCE -> {
                        addAll(favOpenList.sortedBy { it.sortingValues.distance })
                        addAll(favOrderAheadList.sortedBy { it.sortingValues.distance })
                        addAll(favClosedList.sortedBy { it.sortingValues.distance })
                        addAll(notFavorited)
                    }
                    POPULARITY -> {
                        addAll(favOpenList.sortedBy { it.sortingValues.popularity }.reversed())
                        addAll(favOrderAheadList.sortedBy { it.sortingValues.popularity }.reversed())
                        addAll(favClosedList.sortedBy { it.sortingValues.popularity }.reversed())
                        addAll(notFavorited)
                    }
                    AVERAGE_PRICE -> {
                        addAll(favOpenList.sortedBy { it.sortingValues.averageProductPrice })
                        addAll(favOrderAheadList.sortedBy { it.sortingValues.averageProductPrice })
                        addAll(favClosedList.sortedBy { it.sortingValues.averageProductPrice })
                        addAll(notFavorited)
                    }
                    DELIVERY_COSTS -> {
                        addAll(favOpenList.sortedBy { it.sortingValues.deliveryCosts })
                        addAll(favOrderAheadList.sortedBy { it.sortingValues.deliveryCosts })
                        addAll(favClosedList.sortedBy { it.sortingValues.deliveryCosts })
                        addAll(notFavorited)
                    }
                    MIN_COSTS -> {
                        addAll(favOpenList.sortedBy { it.sortingValues.minCost })
                        addAll(favOrderAheadList.sortedBy { it.sortingValues.minCost })
                        addAll(favClosedList.sortedBy { it.sortingValues.minCost })
                        addAll(notFavorited)
                    }
                    WORDS -> {
                        when {
                            !word.isNullOrEmpty() -> addAll(restaurants.filter { it.name.contains(word, ignoreCase = true) })
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