package com.cassianomenezes.restaurantsapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cassianomenezes.restaurantsapp.database.AppDatabase
import com.cassianomenezes.restaurantsapp.database.RestaurantDao
import com.cassianomenezes.restaurantsapp.database.RestaurantObject
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RestaurantDAOTest {

    companion object {
        val restaurant1 = RestaurantObject(0, "Sushi Restaurant")
        val restaurant2 = RestaurantObject(1, "Mama Mia Restaurant")
    }

    private lateinit var appDatabase: AppDatabase //the db instance
    private lateinit var restaurantDao: RestaurantDao //the dao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        restaurantDao = appDatabase.restaurantDao()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun currentDataShouldReturnInsertion() = runBlocking {
        restaurantDao.insertAll(restaurant1, restaurant2)
        val result = restaurantDao.getAll()

        assertEquals(listOf(restaurant1, restaurant2), result)
    }

    @Test
    fun currentDataShouldReturnDeletion() = runBlocking {
        restaurantDao.run {
            insertAll(restaurant1, restaurant2)
            delete(restaurant1.title)
        }
        val result = restaurantDao.getAll()

        assertEquals(listOf(restaurant2), result)
    }

    @Test
    fun getRestaurantById() = runBlocking {
        restaurantDao.insertAll(restaurant1, restaurant2)
        var result: RestaurantObject? = null
        restaurant2.id?.let { result = restaurantDao.getById(it.toString()) }

        assertEquals(restaurant2, result)
    }

    @Test
    fun getRestaurantByName() = runBlocking {
        restaurantDao.insertAll(restaurant1, restaurant2)
        val result = restaurantDao.findByTitle(restaurant1.title)

        assertEquals(restaurant1, result)
    }
}