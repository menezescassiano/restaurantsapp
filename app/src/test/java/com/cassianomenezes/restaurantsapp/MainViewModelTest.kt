package com.cassianomenezes.restaurantsapp

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cassianomenezes.restaurantsapp.database.AppDatabase
import com.cassianomenezes.restaurantsapp.database.RestaurantDao
import com.cassianomenezes.restaurantsapp.database.RestaurantRepositoryImpl
import com.cassianomenezes.restaurantsapp.home.view.viewmodel.MainViewModel
import com.cassianomenezes.restaurantsapp.internal.StatusConstants
import com.cassianomenezes.restaurantsapp.repository.DataRepository
import io.mockk.clearAllMocks
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], application = ApplicationTest::class)
class MainViewModelTest {

    private lateinit var viewModelMock: MainViewModel
    private lateinit var repository: DataRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var restaurantDao: RestaurantDao
    private lateinit var restaurantRepository: RestaurantRepositoryImpl

    @Before
    fun init() {
        clearAllMocks()
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        restaurantDao = appDatabase.restaurantDao()

        restaurantRepository = RestaurantRepositoryImpl(restaurantDao)
        repository = DataRepository(context)

        viewModelMock = MainViewModel(repository, restaurantRepository)
    }

    @Test
    fun `checks if the list of recipes is not empty`() {
        viewModelMock.run {
            val result = getOverallData()
            Assert.assertTrue(result.restaurants.isNotEmpty())
        }
    }

    @Test
    fun `checks if Sushi with initial order returns`() {
        viewModelMock.run {
            val result = getDesiredOrder("sushi", getOverallData().restaurants, StatusConstants.INITIAL)
            Assert.assertTrue(result[0].name.contains("Sushi"))
        }
    }

    @Test
    fun `checks if Nearest restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.DISTANCE)
            Assert.assertTrue(result[0].name == "Tanoshii Sushi")
        }
    }

    @Test
    fun `checks if Best Match restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.BEST_MATCH)
            Assert.assertTrue(result[0].name == "Lunchpakketdienst")
        }
    }

    @Test
    fun `checks if Newest restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.NEWEST)
            Assert.assertTrue(result[0].name == "Indian Kitchen")
        }
    }

    @Test
    fun `checks if highest Rating Average restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.RATING_AVERAGE)
            Assert.assertTrue(result[0].name == "Yvonne's Vispaleis")
        }
    }

    @Test
    fun `checks if highest Popularity restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.POPULARITY)
            Assert.assertTrue(result[0].name == "Tandoori Express")
        }
    }

    @Test
    fun `checks if Average Product price restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.AVERAGE_PRICE)
            Assert.assertTrue(result[0].name == "Lale Restaurant & Snackbar")
        }
    }

    @Test
    fun `checks if lowest Delivery Costs restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.DELIVERY_COSTS)
            Assert.assertTrue(result[0].name == "Sushi One")
        }
    }

    @Test
    fun `checks if Minimum Costs restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.MIN_COSTS)
            Assert.assertTrue(result[0].name == "De Amsterdamsche Tram")
        }
    }

    @Test
    fun `checks if first Open restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.OPEN)
            Assert.assertTrue(result[0].name == "Tanoshii Sushi")
        }
    }

    @Test
    fun `checks if first Closed restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.CLOSED)
            Assert.assertTrue(result[0].name == "Tandoori Express")
        }
    }

    @Test
    fun `checks if first Order Ahead restaurant returns`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.ORDER_AHEAD)
            Assert.assertTrue(result[0].name == "Royal Thai")
        }
    }

    @Test
    fun `checks if favorited restaurant returns in first position`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.INITIAL)
            result[2].added = true
            val newResult = getDesiredOrder(null, result, StatusConstants.INITIAL)
            Assert.assertTrue(newResult[0].name == "Roti Shop")
        }
    }

    @Test
    fun `checks if favorited restaurants return in first, second and third positions`() {
        viewModelMock.run {
            val result = getDesiredOrder(null, getOverallData().restaurants, StatusConstants.INITIAL)
            result[2].added = true
            result[8].added = true
            result[15].added = true
            val newResult = getDesiredOrder(null, result, StatusConstants.INITIAL)
            Assert.assertTrue(newResult[0].name == "Roti Shop")
            Assert.assertTrue(newResult[1].name == "Royal Thai")
            Assert.assertTrue(newResult[2].name == "Tandoori Express")
        }
    }
}