package com.cassianomenezes.restaurantsapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM restaurant")
    suspend fun getAll(): List<RestaurantObject>

    @Query("SELECT * FROM restaurant WHERE id IN (:id) LIMIT 1")
    suspend fun getById(id: String): RestaurantObject

    @Query("SELECT * FROM restaurant WHERE title LIKE :title LIMIT 1")
    suspend fun findByTitle(title: String): RestaurantObject

    @Insert
    suspend fun insertAll(vararg restaurants: RestaurantObject)

    @Delete
    suspend fun delete(restaurantObject: RestaurantObject)

}